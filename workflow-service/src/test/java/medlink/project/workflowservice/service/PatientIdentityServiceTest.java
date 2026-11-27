package medlink.project.workflowservice.service;

import medlink.grpc.auth.v1.PatientResponse;
import medlink.project.workflowservice.client.AuthGrpcClient;
import medlink.project.workflowservice.exception.WorkflowStateException;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PatientIdentityServiceTest {

    private final AuthGrpcClient authClient = mock(AuthGrpcClient.class);
    private final PatientIdentityService service = new PatientIdentityService(authClient);

    @Test
    void resolvesBusinessIdFromAuthenticatedPatientToken() {
        when(authClient.resolveActivePatient("kc-patient")).thenReturn(patient(true, 42L));

        assertThat(service.requireActivePatientId(jwt())).isEqualTo(42L);
    }

    @Test
    void rejectsDisabledPatient() {
        when(authClient.resolveActivePatient("kc-patient")).thenReturn(patient(false, 0L));

        assertThatThrownBy(() -> service.requireActivePatientId(jwt()))
                .isInstanceOf(WorkflowStateException.class)
                .hasMessageContaining("désactivé");
    }

    @Test
    void rejectsMissingBusinessId() {
        when(authClient.resolveActivePatient("kc-patient")).thenReturn(patient(true, 0L));

        assertThatThrownBy(() -> service.requireActivePatientId(jwt()))
                .isInstanceOf(WorkflowStateException.class)
                .hasMessageContaining("identifiant patient métier");
    }

    private PatientResponse patient(boolean enabled, long businessPatientId) {
        return PatientResponse.newBuilder()
                .setId("kc-patient")
                .setEnabled(enabled)
                .setBusinessPatientId(businessPatientId)
                .build();
    }

    private Jwt jwt() {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("kc-patient")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
    }
}
