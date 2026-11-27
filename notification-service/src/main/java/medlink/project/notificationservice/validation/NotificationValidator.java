package medlink.project.notificationservice.validation;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.entity.Notification;
import medlink.project.notificationservice.entity.RecipientType;
import medlink.project.notificationservice.exception.NotificationNotFoundException;
import medlink.project.notificationservice.exception.NotificationValidationException;
import medlink.project.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.Set;

@Component
public class NotificationValidator {

    private final NotificationRepository repository;

    public NotificationValidator(NotificationRepository repository) {
        this.repository = repository;
    }

    public void validateCreate(CreateNotificationRequest request) {
        if (request.sendEmail() && (request.email() == null || request.email().isBlank())) {
            throw new NotificationValidationException(
                    "Une adresse e-mail est obligatoire lorsque sendEmail=true."
            );
        }
    }

    public Notification requireOwned(
            UUID id,
            String recipientUserId,
            Set<RecipientType> allowedRecipientTypes
    ) {
        if (id == null || recipientUserId == null || recipientUserId.isBlank()) {
            throw new NotificationValidationException("Identifiant de notification invalide.");
        }
        return repository.findByIdAndRecipientUserIdAndRecipientTypeIn(
                        id, recipientUserId, allowedRecipientTypes
                )
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification introuvable."
                ));
    }
}
