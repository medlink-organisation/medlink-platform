package medlink.project.notificationservice.service;

import medlink.project.notificationservice.dto.EmailNotificationResponse;
import medlink.project.notificationservice.dto.SendEmailRequest;
import medlink.project.notificationservice.exception.NotificationDeliveryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${medlink.notification.mail.from}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public EmailNotificationResponse send(SendEmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(request.to());
        message.setSubject(request.subject());
        message.setText(request.body());
        try {
            mailSender.send(message);
        } catch (MailException exception) {
            throw new NotificationDeliveryException(
                    "L'envoi de la notification a échoué.", exception
            );
        }
        return new EmailNotificationResponse(
                UUID.randomUUID(), "SENT", Instant.now(), request.correlationId()
        );
    }
}
