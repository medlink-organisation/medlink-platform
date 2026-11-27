package medlink.project.workflowservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendNotificationRequest(
        @NotBlank String recipientUserId,
        @NotBlank String recipientType,
        @NotBlank String notificationType,
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 5000) String message,
        @Email String email,
        boolean sendEmail,
        @Size(max = 100) String correlationId
) {
}
