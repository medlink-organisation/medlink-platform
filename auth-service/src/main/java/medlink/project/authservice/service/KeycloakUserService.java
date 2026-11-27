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
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.ws.rs.NotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private static final String PATIENT_ROLE = "PATIENT";
    private static final String PRATICIEN_ROLE = "PRATICIEN";
    private static final String PATIENT_ID_ATTRIBUTE = "patient_id";

    private final Keycloak keycloak;
    private final KeycloakAdminProperties props;
    private final PatientValidator patientValidator;

    public String createUser(CreatePraticienRequest request) {

        UserRepresentation user = buildPraticienUser(request);

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

    UserRepresentation buildPraticienUser(CreatePraticienRequest request) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(request.email());
        user.setEmail(request.email());
        user.setFirstName(request.prenom());
        user.setLastName(request.nom());

        user.setEnabled(true);
        user.setEmailVerified(false);

        CredentialRepresentation temporaryPassword = new CredentialRepresentation();
        temporaryPassword.setType(CredentialRepresentation.PASSWORD);
        temporaryPassword.setValue(props.getPraticienTemporaryPassword());
        temporaryPassword.setTemporary(true);
        user.setCredentials(List.of(temporaryPassword));

        return user;
    }

    public void assignPraticienRole(String keycloakId) {

        RealmResource realm = keycloak.realm(props.getRealm());

        RoleRepresentation role = realm.roles()
                .get(PRATICIEN_ROLE)
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

            if (!isPatientAccount(user, userResource.roles().realmLevel().listEffective())) {
                throw patientNotFound(normalizedId);
            }

            return PatientResponse.from(user);
        } catch (NotFoundException exception) {
            throw patientNotFound(normalizedId);
        }
    }

    public PatientResponse resolveActivePatient(String keycloakId) {
        String normalizedId = patientValidator.requireKeycloakId(keycloakId);
        try {
            RealmResource realm = keycloak.realm(props.getRealm());
            UserResource userResource = realm.users().get(normalizedId);
            UserRepresentation user = userResource.toRepresentation();

            if (!isPatientAccount(user, userResource.roles().realmLevel().listEffective())) {
                throw patientNotFound(normalizedId);
            }
            if (!Boolean.TRUE.equals(user.isEnabled())) {
                return PatientResponse.from(user, 0L);
            }

            return PatientResponse.from(
                    user,
                    resolveBusinessPatientId(
                            realm,
                            userResource,
                            user,
                            normalizedId
                    )
            );
        } catch (NotFoundException exception) {
            throw patientNotFound(normalizedId);
        }
    }

    public PatientPageResponse getAllPatients(int first, int max) {

        patientValidator.validatePagination(first, max);

        RealmResource realm = keycloak.realm(props.getRealm());

        List<UserRepresentation> patients = realm.roles()
                .get(PATIENT_ROLE)
                .getUserMembers(first, max + 1);

        boolean hasNext = patients.size() > max;

        List<PatientResponse> content = patients.stream()
                .limit(max)
                .map(PatientResponse::from)
                .toList();

        return new PatientPageResponse(
                content,
                first,
                max,
                hasNext
        );
    }
    public PatientPageResponse getAllPatients() {
        return getAllPatients(0, 20);
    }

    public List<PatientResponse> getPatientsByBusinessIds(
            Collection<Long> businessPatientIds
    ) {
        if (businessPatientIds == null) {
            throw new IllegalArgumentException("La liste des patients est obligatoire");
        }

        LinkedHashSet<Long> ids = new LinkedHashSet<>(businessPatientIds);
        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new IllegalArgumentException("Un identifiant patient est invalide");
        }
        if (ids.size() > 200) {
            throw new IllegalArgumentException("Trop de patients demandés");
        }

        RealmResource realm = keycloak.realm(props.getRealm());
        return ids.stream()
                .map(id -> findPatientByBusinessId(realm, id))
                .flatMap(Optional::stream)
                .toList();
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
        RealmResource realm = keycloak.realm(props.getRealm());
        try {
            UserRepresentation user = realm.users().get(keycloakId).toRepresentation();
            return isPatientAccount(realm, user);
        } catch (NotFoundException exception) {
            return false;
        }
    }

    private boolean isPatientAccount(RealmResource realm, UserRepresentation user) {
        try {
            return isPatientAccount(user, realm
                    .users()
                    .get(user.getId())
                    .roles()
                    .realmLevel()
                    .listEffective());
        } catch (NotFoundException exception) {
            return false;
        }
    }

    private Optional<PatientResponse> findPatientByBusinessId(
            RealmResource realm,
            long businessPatientId
    ) {
        String expected = Long.toString(businessPatientId);
        List<UserRepresentation> matches = realm.users()
                .searchByAttributes(
                        0,
                        2,
                        null,
                        false,
                        PATIENT_ID_ATTRIBUTE + ":" + expected
                )
                .stream()
                .filter(user -> expected.equals(
                        user.firstAttribute(PATIENT_ID_ATTRIBUTE)))
                .filter(user -> isPatientAccount(realm, user))
                .toList();

        if (matches.size() > 1) {
            throw new IllegalStateException(
                    "Plusieurs patients utilisent le même identifiant métier"
            );
        }
        return matches.stream()
                .findFirst()
                .map(user -> PatientResponse.from(user, businessPatientId));
    }

    private boolean isPatientAccount(
            UserRepresentation user,
            List<RoleRepresentation> roles
    ) {
        if (user.getServiceAccountClientId() != null
                || (user.getUsername() != null
                && user.getUsername().startsWith("service-account-"))) {
            return false;
        }

        boolean patient = false;
        for (RoleRepresentation role : roles) {
            if (PATIENT_ROLE.equals(role.getName())) {
                patient = true;
            }
            if (PRATICIEN_ROLE.equals(role.getName())
                    || "ADMIN".equals(role.getName())) {
                return false;
            }
        }
        return patient;
    }

    private synchronized long resolveBusinessPatientId(
            RealmResource realm,
            UserResource userResource,
            UserRepresentation user,
            String keycloakId
    ) {
        long existingId = parsePositivePatientId(
                user.firstAttribute(PATIENT_ID_ATTRIBUTE)
        );
        if (existingId > 0) {
            return existingId;
        }

        for (int salt = 0; salt < 100; salt++) {
            long candidate = stablePatientId(keycloakId, salt);
            if (!patientIdUsedByAnotherUser(realm, keycloakId, candidate)) {
                user.singleAttribute(PATIENT_ID_ATTRIBUTE, Long.toString(candidate));
                userResource.update(user);
                return candidate;
            }
        }

        throw new IllegalStateException(
                "Impossible d'allouer un identifiant métier au patient"
        );
    }

    private boolean patientIdUsedByAnotherUser(
            RealmResource realm,
            String keycloakId,
            long candidate
    ) {
        String expected = Long.toString(candidate);
        return realm.users()
                .searchByAttributes(
                        0,
                        10,
                        null,
                        false,
                        PATIENT_ID_ATTRIBUTE + ":" + expected
                )
                .stream()
                .anyMatch(user -> !keycloakId.equals(user.getId())
                        && expected.equals(user.firstAttribute(PATIENT_ID_ATTRIBUTE)));
    }

    private long stablePatientId(String keycloakId, int salt) {
        UUID source;
        try {
            source = UUID.fromString(keycloakId);
        } catch (IllegalArgumentException exception) {
            source = UUID.nameUUIDFromBytes(
                    keycloakId.getBytes(StandardCharsets.UTF_8)
            );
        }

        long mixed = source.getMostSignificantBits()
                ^ Long.rotateLeft(source.getLeastSignificantBits(), 21)
                ^ (0x9E3779B97F4A7C15L * salt);
        long candidate = mixed & Long.MAX_VALUE;
        return candidate == 0 ? salt + 1L : candidate;
    }

    private long parsePositivePatientId(String value) {
        if (value == null || value.isBlank()) {
            return -1L;
        }
        try {
            long patientId = Long.parseLong(value);
            return patientId > 0 ? patientId : -1L;
        } catch (NumberFormatException exception) {
            return -1L;
        }
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
