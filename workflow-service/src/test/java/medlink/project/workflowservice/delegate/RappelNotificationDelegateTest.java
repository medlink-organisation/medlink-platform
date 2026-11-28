package medlink.project.workflowservice.delegate;

import medlink.grpc.auth.v1.PatientResponse;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.project.workflowservice.client.AuthGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.service.NotificationService;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RappelNotificationDelegateTest {

    @Test
    void envoieLeRappelApplicationEtEmailAuxDeuxParticipantsSansIdentifiantVisible() {
        NotificationService notificationService = mock(NotificationService.class);
        AuthGrpcClient authGrpcClient = mock(AuthGrpcClient.class);
        PraticienGrpcClient praticienGrpcClient = mock(PraticienGrpcClient.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        LocalDateTime dateRdv = LocalDateTime.of(2026, 12, 1, 9, 30);

        when(execution.getVariable("patientId")).thenReturn(42L);
        when(execution.getVariable("praticienId")).thenReturn(56L);
        when(execution.getVariable("dateRdv")).thenReturn(dateRdv);
        when(execution.getProcessInstanceId()).thenReturn("process-1");
        when(authGrpcClient.getPatientByBusinessId(42L)).thenReturn(PatientResponse.newBuilder()
                .setId("kc-patient")
                .setEmail("patient@medlink.test")
                .build());
        when(praticienGrpcClient.getById(56L)).thenReturn(PraticienGrpcResponse.newBuilder()
                .setKeycloakId("kc-praticien")
                .setEmail("praticien@medlink.test")
                .build());

        new RappelNotificationDelegate(
                notificationService,
                authGrpcClient,
                praticienGrpcClient
        ).execute(execution);

        ArgumentCaptor<SendNotificationRequest> captor =
                ArgumentCaptor.forClass(SendNotificationRequest.class);
        verify(notificationService, times(2)).sendNotification(captor.capture());
        assertThat(captor.getAllValues()).allSatisfy(notification -> {
            assertThat(notification.sendEmail()).isTrue();
            assertThat(notification.notificationType()).isEqualTo("RENDEZ_VOUS_RAPPEL");
            assertThat(notification.message()).doesNotContain("42", "56", "process-1");
        });
        assertThat(captor.getAllValues())
                .extracting(SendNotificationRequest::recipientType)
                .containsExactly("PATIENT", "PRATICIEN");
    }
}
