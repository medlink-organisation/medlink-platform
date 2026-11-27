package medlink.project.authservice.service;

import medlink.project.authservice.confi.KeycloakAdminProperties;
import medlink.project.authservice.dto.CreatePraticienRequest;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakUserServiceTest {

    @Test
    void buildsPraticienWithTemporaryPassword() {
        KeycloakAdminProperties properties = new KeycloakAdminProperties();
        properties.setPraticienTemporaryPassword("Pratcien@123456");
        KeycloakUserService service = new KeycloakUserService(null, properties, null);
        CreatePraticienRequest request = new CreatePraticienRequest(
                "Ben Salah",
                "Amine",
                "amine@example.com",
                "+21620123456",
                "ORD-123",
                1L
        );

        UserRepresentation user = service.buildPraticienUser(request);

        assertThat(user.getUsername()).isEqualTo("amine@example.com");
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getCredentials()).hasSize(1);

        CredentialRepresentation credential = user.getCredentials().getFirst();
        assertThat(credential.getType()).isEqualTo(CredentialRepresentation.PASSWORD);
        assertThat(credential.getValue()).isEqualTo("Pratcien@123456");
        assertThat(credential.isTemporary()).isTrue();
    }
}
