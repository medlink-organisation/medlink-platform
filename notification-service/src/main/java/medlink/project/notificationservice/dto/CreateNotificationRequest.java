package medlink.project.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import medlink.project.notificationservice.entity.NotificationType;
import medlink.project.notificationservice.entity.RecipientType;

public record CreateNotificationRequest(
        @NotBlank @Size(max = 255) String recipientUserId,
        @NotNull RecipientType recipientType,
        @NotNull NotificationType notificationType,
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 5000) String message,
        @Email @Size(max = 320) String email,
        boolean sendEmail,
        @Size(max = 100) String correlationId
) {
}
