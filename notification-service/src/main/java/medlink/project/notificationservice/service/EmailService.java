package medlink.project.notificationservice.service;

import medlink.project.notificationservice.dto.EmailNotificationResponse;
import medlink.project.notificationservice.dto.SendEmailRequest;

public interface EmailService {

    EmailNotificationResponse send(SendEmailRequest request);
}
