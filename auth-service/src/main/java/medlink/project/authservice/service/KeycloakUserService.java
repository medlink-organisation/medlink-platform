package medlink.project.authservice.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import medlink.project.authservice.confi.KeycloakAdminProperties;
import medlink.project.authservice.dto.CreatePraticienRequest;
import medlink.project.authservice.dto.PatientPageResponse;
import medlink.project.authservice.dto.PatientResponse;
import medlink.project.authservice.dto.UpdatePatientContactRequest;
import medlink.project.authservice.validation.PatientValidator;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.ws.rs.NotFoundException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private static final String PATIENT_ROLE = "PATIENT";

    private final Keycloak keycloak;
    private final KeycloakAdminProperties props;
    private final PatientValidator patientValidator;

    public String createUser(CreatePraticienRequest request) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(request.email());
        user.setEmail(request.email());
        user.setFirstName(request.prenom());
        user.setLastName(request.nom());

        user.setEnabled(false);
        user.setEmailVerified(false);

        RealmResource realm = keycloak.realm(props.getRealm());

        try (Response response = realm.users().create(user)) {

            if (response.getStatus() == 409) {
                throw new IllegalStateException(
                        "Email déjà utilisé dans Keycloak"
                );
            }

            if (response.getStatus() != 201) {
                throw new IllegalStateException(
                        "Création Keycloak échouée : HTTP "
                                + response.getStatus()
                );
            }

            return CreatedResponseUtil.getCreatedId(response);
        }
    }

    public void assignPraticienRole(String keycloakId) {

        RealmResource realm = keycloak.realm(props.getRealm());

        RoleRepresentation role = realm.roles()
                .get("PRATICIEN")
                .toRepresentation();

        realm.users()
                .get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    public void enableUser(String keycloakId) {

        var userResource = keycloak
                .realm(props.getRealm())
                .users()
                .get(keycloakId);

        UserRepresentation user = userResource.toRepresentation();

        user.setEnabled(true);

        userResource.update(user);
    }

    public void sendActivationEmail(String keycloakId) {

        keycloak.realm(props.getRealm())
                .users()
                .get(keycloakId)
                .executeActionsEmail(
                        List.of("VERIFY_EMAIL", "UPDATE_PASSWORD")
                );
    }

    public void deleteUser(String keycloakId) {

        try (Response response = keycloak
                .realm(props.getRealm())
                .users()
                .delete(keycloakId)) {

            if (response.getStatus() != 204
                    && response.getStatus() != 404) {

                throw new IllegalStateException(
                        "Suppression Keycloak échouée : HTTP "
                                + response.getStatus()
                );
            }
        }
    }

    public UserRepresentation getUser(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("keycloakId obligatoire");
        }
        return keycloak.realm(props.getRealm())
                .users()
                .get(keycloakId)
                .toRepresentation();
    }

    public PatientResponse getPatientById(String keycloakId) {
        String normalizedId = patientValidator.requireKeycloakId(keycloakId);

        try {
            RealmResource realm = keycloak.realm(props.getRealm());
            var userResource = realm.users().get(normalizedId);
            UserRepresentation user = userResource.toRepresentation();

            if (!hasPatientRole(userResource.roles().realmLevel().listEffective())) {
                throw patientNotFound(normalizedId);
            }

            return PatientResponse.from(user);
        } catch (NotFoundException exception) {
            throw patientNotFound(normalizedId);
        }
    }

    public PatientPageResponse getAllPatients(int first, int max) {
        patientValidator.validatePagination(first, max);

        try {
            var patientRole = keycloak.realm(props.getRealm())
                    .roles()
                    .get(PATIENT_ROLE);

            List<UserRepresentation> page = patientRole.getUserMembers(first, max + 1);
            boolean hasNext = page.size() > max;
            List<PatientResponse> content = page.stream()
                    .limit(max)
                    .map(PatientResponse::from)
                    .toList();

            return new PatientPageResponse(content, first, max, hasNext);
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Le rôle " + PATIENT_ROLE + " n'existe pas dans Keycloak",
                    exception
            );
        }
    }

    public PatientPageResponse getAllPatients() {
        return getAllPatients(0, 20);
    }

    public PatientResponse getPatientByEmail(String email) {
        String normalizedEmail = patientValidator.requireEmail(email);

        return keycloak.realm(props.getRealm())
                .users()
                .searchByEmail(normalizedEmail, true)
                .stream()
                .filter(user -> normalizedEmail.equalsIgnoreCase(user.getEmail()))
                .filter(user -> hasPatientRole(user.getId()))
                .findFirst()
                .map(PatientResponse::from)
                .orElseThrow(() -> patientNotFoundByEmail(normalizedEmail));
    }

    public boolean existsPatientById(String keycloakId) {
        try {
            getPatientById(keycloakId);
            return true;
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw exception;
        }
    }

    public boolean existsPatientByEmail(String email) {
        try {
            getPatientByEmail(email);
            return true;
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
            throw exception;
        }
    }

    public boolean isPatientActive(String keycloakId) {
        return getPatientById(keycloakId).enabled();
    }

    public void enablePatient(String keycloakId) {
        updatePatientEnabled(keycloakId, true);
    }

    public void disablePatient(String keycloakId) {
        updatePatientEnabled(keycloakId, false);
    }

    public List<String> getPatientRoles(String keycloakId) {
        String normalizedId = patientValidator.requireKeycloakId(keycloakId);
        getPatientById(normalizedId);

        return keycloak.realm(props.getRealm())
                .users()
                .get(normalizedId)
                .roles()
                .realmLevel()
                .listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public PatientResponse updatePatientContact(
            String keycloakId,
            UpdatePatientContactRequest request
    ) {
        String normalizedId = patientValidator.requireKeycloakId(keycloakId);
        patientValidator.validateContact(request);
        getPatientById(normalizedId);

        var users = keycloak.realm(props.getRealm()).users();
        var userResource = users.get(normalizedId);
        UserRepresentation user = userResource.toRepresentation();

        if (request.email() != null) {
            String email = patientValidator.requireEmail(request.email());
            boolean emailAlreadyUsed = users.searchByEmail(email, true)
                    .stream()
                    .anyMatch(existing -> !normalizedId.equals(existing.getId()));

            if (emailAlreadyUsed) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email déjà utilisé dans Keycloak"
                );
            }
            user.setEmail(email);
            user.setUsername(email);
        }
        if (request.firstName() != null) {
            user.setFirstName(request.firstName().trim());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName().trim());
        }

        userResource.update(user);
        return PatientResponse.from(userResource.toRepresentation());
    }

    private void updatePatientEnabled(String keycloakId, boolean enabled) {
        String normalizedId = patientValidator.requireKeycloakId(keycloakId);
        getPatientById(normalizedId);

        var userResource = keycloak.realm(props.getRealm())
                .users()
                .get(normalizedId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(enabled);
        userResource.update(user);
    }

    private boolean hasPatientRole(String keycloakId) {
        try {
            return hasPatientRole(keycloak.realm(props.getRealm())
                    .users()
                    .get(keycloakId)
                    .roles()
                    .realmLevel()
                    .listEffective());
        } catch (NotFoundException exception) {
            return false;
        }
    }

    private boolean hasPatientRole(List<RoleRepresentation> roles) {
        return roles.stream().anyMatch(role -> PATIENT_ROLE.equals(role.getName()));
    }

    private ResponseStatusException patientNotFound(String keycloakId) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Patient Keycloak introuvable : " + keycloakId
        );
    }

    private ResponseStatusException patientNotFoundByEmail(String email) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Patient Keycloak introuvable pour l'email : " + email
        );
    }
}
