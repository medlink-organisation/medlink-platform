package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.AuthGrpcClient;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.service.NotificationService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("rappelNotificationDelegate")
@RequiredArgsConstructor
public class RappelNotificationDelegate implements JavaDelegate {

    private final NotificationService notificationService;
    private final AuthGrpcClient authGrpcClient;
    private final PraticienGrpcClient praticienGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        Object value = execution.getVariable("patientId");
        if (!(value instanceof Number number) || number.longValue() <= 0) {
            throw new IllegalArgumentException("patientId obligatoire");
        }
        var patient = authGrpcClient.getPatientByBusinessId(number.longValue());
        notificationService.sendNotification(new SendNotificationRequest(
                patient.getId(),
                "PATIENT",
                "RENDEZ_VOUS_RAPPEL",
                "Rappel de votre rendez-vous MedLink",
                "Votre rendez-vous est prévu le " + execution.getVariable("dateRdv") + ".",
                patient.getEmail(),
                true,
                execution.getProcessInstanceId() + ":rappel"
        ));

        Object praticienId = execution.getVariable("praticienId");
        if (!(praticienId instanceof Number praticienNumber) || praticienNumber.longValue() <= 0) {
            throw new IllegalArgumentException("praticienId obligatoire");
        }
        var praticien = praticienGrpcClient.getById(praticienNumber.longValue());
        notificationService.sendNotification(new SendNotificationRequest(
                praticien.getKeycloakId(),
                "PRATICIEN",
                "RENDEZ_VOUS_RAPPEL",
                "Rappel d'un rendez-vous MedLink",
                "Un rendez-vous est prévu le " + execution.getVariable("dateRdv") + ".",
                praticien.getEmail(),
                true,
                execution.getProcessInstanceId() + ":praticien-rappel"
        ));
    }
}
