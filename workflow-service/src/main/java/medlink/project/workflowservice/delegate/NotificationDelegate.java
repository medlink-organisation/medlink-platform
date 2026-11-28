package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.AuthGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.service.NotificationService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("notificationDelegate")
@RequiredArgsConstructor
public class NotificationDelegate implements JavaDelegate {

    private final NotificationService notificationService;
    private final AuthGrpcClient authGrpcClient;
    private final PraticienGrpcClient praticienGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        var patient = authGrpcClient.getPatientByBusinessId(requiredLong(execution, "patientId"));
        Object dateRdv = execution.getVariable("dateRdv");

        notificationService.sendNotification(new SendNotificationRequest(
                patient.getId(),
                "PATIENT",
                "RENDEZ_VOUS_CONFIRMATION",
                "Confirmation de votre rendez-vous MedLink",
                "Votre rendez-vous est confirmé pour le " + dateRdv + ".",
                patient.getEmail(),
                true,
                execution.getProcessInstanceId()
        ));

        var praticien = praticienGrpcClient.getById(requiredLong(execution, "praticienId"));
        notificationService.sendNotification(new SendNotificationRequest(
                praticien.getKeycloakId(),
                "PRATICIEN",
                "RENDEZ_VOUS_CONFIRMATION",
                "Nouveau rendez-vous MedLink",
                "Un rendez-vous est confirmé pour le " + dateRdv + ".",
                praticien.getEmail(),
                true,
                execution.getProcessInstanceId() + ":praticien"
        ));
    }

    private String requiredString(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof String string && !string.isBlank()) {
            return string;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }

    private long requiredLong(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Number number && number.longValue() > 0) {
            return number.longValue();
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }
}
