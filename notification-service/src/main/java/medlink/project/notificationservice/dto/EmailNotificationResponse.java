package medlink.project.notificationservice.dto;

import java.time.Instant;
import java.util.UUID;

public record EmailNotificationResponse(
        UUID notificationId,
        String status,
        Instant sentAt,
        String correlationId
) {
}
