package medlink.project.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendEmailRequest(
        @NotBlank @Email String to,
        @NotBlank @Size(max = 255) String subject,
        @NotBlank @Size(max = 20_000) String body,
        @Size(max = 100) String correlationId
) {
}
