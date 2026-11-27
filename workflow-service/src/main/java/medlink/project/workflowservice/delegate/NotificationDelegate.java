package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.AuthGrpcClient;
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

    @Override
    public void execute(DelegateExecution execution) {
        String email = resolvePatientEmail(execution);
        String patientUserId = requiredString(execution, "actorUserId");
        Object dateRdv = execution.getVariable("dateRdv");

        notificationService.sendNotification(new SendNotificationRequest(
                patientUserId,
                "PATIENT",
                "RENDEZ_VOUS_CONFIRMATION",
                "Confirmation de votre rendez-vous MedLink",
                "Votre rendez-vous est confirmé pour le " + dateRdv + ".",
                email,
                true,
                execution.getProcessInstanceId()
        ));
    }

    private String resolvePatientEmail(DelegateExecution execution) {
        Object patientEmail = execution.getVariable("patientEmail");
        if (patientEmail instanceof String email && !email.isBlank()) {
            return email;
        }

        String actorUserId = requiredString(execution, "actorUserId");
        String email = authGrpcClient.getUserContact(actorUserId).getEmail();
        execution.setVariable("patientEmail", email);
        return email;
    }

    private String requiredString(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof String string && !string.isBlank()) {
            return string;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }
}
