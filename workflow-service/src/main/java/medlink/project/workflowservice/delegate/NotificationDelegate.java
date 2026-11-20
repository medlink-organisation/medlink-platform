package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.AuthGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("notificationDelegate")
@RequiredArgsConstructor
public class NotificationDelegate implements JavaDelegate {

    private final JavaMailSender mailSender;
    private final AuthGrpcClient authGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        String email = resolvePatientEmail(execution);
        Object dateRdv = execution.getVariable("dateRdv");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirmation de votre rendez-vous MedLink");
        message.setText("Votre rendez-vous est confirmé pour le " + dateRdv + ".");
        mailSender.send(message);
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
