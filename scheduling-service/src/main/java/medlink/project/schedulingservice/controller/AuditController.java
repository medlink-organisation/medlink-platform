package medlink.project.schedulingservice.controller;

import medlink.project.auditcore.dto.AuditOperation;
import medlink.project.auditcore.dto.AuditRevisionDto;
import medlink.project.auditcore.service.AuditFilter;
import medlink.project.schedulingservice.service.AuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit")
@PreAuthorize("hasAuthority('AUDIT_READ')")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public Page<AuditRevisionDto> search(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) AuditOperation operation,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return auditService.search(new AuditFilter(
                entityType, null, null, userId, operation,
                startDate == null ? null : startDate.atStartOfDay().toInstant(ZoneOffset.UTC),
                endDate == null ? null : endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        ), auditPageable(pageable));
    }

    @GetMapping("/entities")
    public List<String> entities() {
        return auditService.getAuditedEntityTypes();
    }

    @GetMapping("/{entityType}/{entityId}")
    public Page<AuditRevisionDto> history(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return auditService.getHistory(entityType, entityId, auditPageable(pageable));
    }

    @GetMapping("/{entityType}/{entityId}/revisions/{revisionId}")
    public AuditRevisionDto revision(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @PathVariable long revisionId
    ) {
        return auditService.getRevision(entityType, entityId, revisionId);
    }

    @GetMapping("/{entityType}/{entityId}/compare")
    public AuditRevisionDto compare(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam long fromRevision,
            @RequestParam long toRevision
    ) {
        return auditService.compare(entityType, entityId, fromRevision, toRevision);
    }

    private Pageable auditPageable(Pageable pageable) {
        Sort fixedSort = Sort.by(
                Sort.Order.desc("revisionId"),
                Sort.Order.asc("entityName"),
                Sort.Order.asc("entityId")
        );
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), fixedSort);
    }
}
