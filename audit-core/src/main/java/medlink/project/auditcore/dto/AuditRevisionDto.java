package medlink.project.auditcore.dto;

import java.time.Instant;
import java.util.List;

public record AuditRevisionDto(
        String entityName,
        String entityId,
        long revisionId,
        Instant timestamp,
        String userId,
        String username,
        ActorType actorType,
        AuditOperation operation,
        String serviceName,
        List<FieldChangeDto> changes
) {
}
