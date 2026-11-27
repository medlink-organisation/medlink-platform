package medlink.project.auditcore.service;

import medlink.project.auditcore.dto.AuditRevisionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditReadService {

    Page<AuditRevisionDto> search(AuditFilter filter, Pageable pageable);

    List<String> getAuditedEntityTypes();

    Page<AuditRevisionDto> getHistory(String entityType, String entityId, Pageable pageable);

    AuditRevisionDto getRevision(String entityType, String entityId, long revisionId);

    AuditRevisionDto compare(String entityType, String entityId, long fromRevision, long toRevision);
}
