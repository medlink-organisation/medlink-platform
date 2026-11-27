package medlink.project.notificationservice.dto;

import medlink.project.notificationservice.entity.NotificationType;
import medlink.project.notificationservice.entity.RecipientType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        RecipientType recipientType,
        NotificationType notificationType,
        String title,
        String message,
        boolean read,
        Instant readAt,
        boolean emailSent,
        String correlationId,
        LocalDateTime createdAt
) {
}
