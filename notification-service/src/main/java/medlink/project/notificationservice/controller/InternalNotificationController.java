package medlink.project.notificationservice.controller;

import jakarta.validation.Valid;
import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.dto.EmailNotificationResponse;
import medlink.project.notificationservice.dto.NotificationDto;
import medlink.project.notificationservice.dto.SendEmailRequest;
import medlink.project.notificationservice.service.EmailService;
import medlink.project.notificationservice.service.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications")
@PreAuthorize("@notificationProducerValidator.isAuthorized(authentication)")
public class InternalNotificationController {

    private final NotificationService notificationService;
    private final EmailService emailService;

    public InternalNotificationController(
            NotificationService notificationService,
            EmailService emailService
    ) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @PostMapping
    public NotificationDto create(@Valid @RequestBody CreateNotificationRequest request) {
        return notificationService.create(request);
    }

    @PostMapping("/email")
    public EmailNotificationResponse sendEmail(@Valid @RequestBody SendEmailRequest request) {
        return emailService.send(request);
    }
}
