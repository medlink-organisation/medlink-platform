package medlink.project.authservice.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import medlink.project.authservice.confi.KeycloakAdminProperties;
import medlink.project.authservice.dto.CreatePraticienRequest;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;
    private final KeycloakAdminProperties props;

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
                .get("ROLE_PRATICIEN")
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
}