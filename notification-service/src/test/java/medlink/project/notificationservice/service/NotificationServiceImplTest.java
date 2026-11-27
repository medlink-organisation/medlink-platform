package medlink.project.notificationservice.service;

import medlink.project.notificationservice.dto.SendEmailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationServiceImplTest {

    @Test
    void sendsEmailAndReturnsDeliveryMetadata() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        EmailService service = new EmailServiceImpl(
                mailSender, "no-reply@medlink.test"
        );

        var response = service.send(new SendEmailRequest(
                "patient@example.com",
                "Confirmation",
                "Votre rendez-vous est confirmé.",
                "workflow-123"
        ));

        verify(mailSender).send(any(SimpleMailMessage.class));
        assertThat(response.status()).isEqualTo("SENT");
        assertThat(response.correlationId()).isEqualTo("workflow-123");
        assertThat(response.notificationId()).isNotNull();
        assertThat(response.sentAt()).isNotNull();
    }
}
