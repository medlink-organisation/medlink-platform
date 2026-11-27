package medlink.project.auditcore.service;

import medlink.project.auditcore.dto.AuditOperation;

import java.time.Instant;

public record AuditFilter(
        String entityType,
        String entityId,
        Long revisionId,
        String userId,
        AuditOperation operation,
        Instant startDate,
        Instant endDate
) {
}
