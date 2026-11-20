package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("alertePraticienDelegate")
@RequiredArgsConstructor
public class AlertePraticienDelegate implements JavaDelegate {

    private final JavaMailSender mailSender;
    private final PraticienGrpcClient praticienGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        String email = resolvePraticienEmail(execution);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Annulation d'un rendez-vous MedLink");
        message.setText("Le rendez-vous n° " + execution.getVariable("rdvId")
                + " a été annulé.");
        mailSender.send(message);
    }

    private String resolvePraticienEmail(DelegateExecution execution) {
        Object praticienEmail = execution.getVariable("praticienEmail");
        if (praticienEmail instanceof String email && !email.isBlank()) {
            return email;
        }

        Object praticienId = execution.getVariable("praticienId");
        if (!(praticienId instanceof Number number)) {
            throw new IllegalArgumentException("praticienId obligatoire");
        }

        String email = praticienGrpcClient.getById(number.longValue()).getEmail();
        execution.setVariable("praticienEmail", email);
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
