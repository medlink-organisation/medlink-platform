package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.service.NotificationService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("alertePraticienDelegate")
@RequiredArgsConstructor
public class AlertePraticienDelegate implements JavaDelegate {

    private final NotificationService notificationService;
    private final PraticienGrpcClient praticienGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        Recipient recipient = resolvePraticien(execution);

        notificationService.sendNotification(new SendNotificationRequest(
                recipient.keycloakId(),
                "PRATICIEN",
                "RENDEZ_VOUS_ANNULATION",
                "Annulation d'un rendez-vous MedLink",
                "Le rendez-vous n° " + execution.getVariable("rdvId") + " a été annulé.",
                recipient.email(),
                true,
                execution.getProcessInstanceId()
        ));
    }

    private Recipient resolvePraticien(DelegateExecution execution) {
        Object praticienEmail = execution.getVariable("praticienEmail");
        Object praticienKeycloakId = execution.getVariable("praticienKeycloakId");
        if (praticienEmail instanceof String email && !email.isBlank()
                && praticienKeycloakId instanceof String keycloakId && !keycloakId.isBlank()) {
            return new Recipient(keycloakId, email);
        }

        Object praticienId = execution.getVariable("praticienId");
        if (!(praticienId instanceof Number number)) {
            throw new IllegalArgumentException("praticienId obligatoire");
        }

        PraticienGrpcResponse praticien = praticienGrpcClient.getById(number.longValue());
        execution.setVariable("praticienEmail", praticien.getEmail());
        execution.setVariable("praticienKeycloakId", praticien.getKeycloakId());
        return new Recipient(praticien.getKeycloakId(), praticien.getEmail());
    }

    private record Recipient(String keycloakId, String email) {
    }
}
