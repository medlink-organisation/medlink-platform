package medlink.project.authservice.service;

import medlink.project.authservice.grpc.PraticienGrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PraticienRegistrationServiceTest {

    private KeycloakUserService keycloakUserService;
    private PraticienRegistrationService registrationService;

    @BeforeEach
    void setUp() {
        keycloakUserService = mock(KeycloakUserService.class);
        registrationService = new PraticienRegistrationService(
                keycloakUserService,
                mock(PraticienGrpcClient.class)
        );
    }

    @Test
    void deletesKeycloakAccount() {
        registrationService.deleteKeycloakAccount("keycloak-user-id");

        verify(keycloakUserService).deleteUser("keycloak-user-id");
    }

    @Test
    void rejectsBlankKeycloakId() {
        assertThatThrownBy(() -> registrationService.deleteKeycloakAccount(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("keycloakId obligatoire");
    }
}
