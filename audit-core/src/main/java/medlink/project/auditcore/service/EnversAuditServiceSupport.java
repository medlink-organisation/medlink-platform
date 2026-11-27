package medlink.project.auditcore.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import medlink.project.auditcore.dto.ActorType;
import medlink.project.auditcore.dto.AuditOperation;
import medlink.project.auditcore.dto.AuditRevisionDto;
import medlink.project.auditcore.dto.FieldChangeDto;
import medlink.project.auditcore.exception.AuditBadRequestException;
import medlink.project.auditcore.exception.AuditRevisionNotFoundException;
import medlink.project.auditcore.mapper.AuditMapper;
import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.auditcore.revision.AuditRevisionEntity;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public abstract class EnversAuditServiceSupport implements AuditReadService {

    private static final int MAX_PAGE_SIZE = 100;

    protected final EntityManager entityManager;
    private final AuditedEntityRegistry registry;
    private final AuditMapper mapper;
    private final String serviceName;

    protected EnversAuditServiceSupport(
            EntityManager entityManager,
            AuditedEntityRegistry registry,
            String serviceName
    ) {
        this.entityManager = entityManager;
        this.registry = registry;
        this.mapper = new AuditMapper(entityManager);
        this.serviceName = serviceName;
    }

    @Override
    public Page<AuditRevisionDto> search(AuditFilter filter, Pageable pageable) {
        validateFilter(filter);
        Pageable safePageable = validatePageable(pageable);
        Collection<AuditedEntityDescriptor> descriptors = filter.entityType() == null
                ? registry.all()
                : List.of(registry.require(filter.entityType()));
        if (descriptors.isEmpty()) {
            return Page.empty(safePageable);
        }

        String union = buildUnion(descriptors, filter);
        Query dataQuery = entityManager.createNativeQuery(
                "SELECT entity_name, entity_id, revision_id, revision_type FROM (" + union + ") audit_union "
                        + "ORDER BY revision_id DESC, entity_name ASC, entity_id ASC"
        );
        bind(dataQuery, filter);
        dataQuery.setFirstResult((int) safePageable.getOffset());
        dataQuery.setMaxResults(safePageable.getPageSize());

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM (" + union + ") audit_count"
        );
        bind(countQuery, filter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQuery.getResultList();
        List<AuditRevisionDto> content = rows.stream().map(this::mapRow).toList();
        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(content, safePageable, total);
    }

    @Override
    public List<String> getAuditedEntityTypes() {
        return registry.all().stream()
                .map(AuditedEntityDescriptor::apiName)
                .sorted()
                .toList();
    }

    @Override
    public Page<AuditRevisionDto> getHistory(String entityType, String entityId, Pageable pageable) {
        AuditedEntityDescriptor descriptor = registry.require(entityType);
        parseId(descriptor, entityId);
        return search(new AuditFilter(entityType, entityId, null, null, null, null, null), pageable);
    }

    @Override
    public AuditRevisionDto getRevision(String entityType, String entityId, long revisionId) {
        if (revisionId <= 0) {
            throw new AuditBadRequestException("L'identifiant de révision doit être positif.");
        }
        AuditedEntityDescriptor descriptor = registry.require(entityType);
        parseId(descriptor, entityId);
        Page<AuditRevisionDto> result = search(
                new AuditFilter(entityType, entityId, revisionId, null, null, null, null),
                Pageable.ofSize(1)
        );
        if (result.isEmpty()) {
            throw new AuditRevisionNotFoundException("Révision introuvable pour cette entité.");
        }
        return result.getContent().getFirst();
    }

    @Override
    public AuditRevisionDto compare(String entityType, String entityId, long fromRevision, long toRevision) {
        if (fromRevision <= 0 || toRevision <= 0 || fromRevision >= toRevision) {
            throw new AuditBadRequestException("La comparaison exige fromRevision < toRevision et des valeurs positives.");
        }
        AuditedEntityDescriptor descriptor = registry.require(entityType);
        Object id = parseId(descriptor, entityId);
        ensureEntityRevisionExists(descriptor, entityId, fromRevision);
        ensureEntityRevisionExists(descriptor, entityId, toRevision);

        AuditReader reader = AuditReaderFactory.get(entityManager);
        Object before = reader.find(descriptor.entityClass(), id, fromRevision);
        Object after = reader.find(descriptor.entityClass(), id, toRevision);
        List<FieldChangeDto> changes = mapper.compare(before, after, descriptor);
        AuditRevisionEntity metadata = findRevisionMetadata(reader, toRevision);
        return dto(descriptor, entityId, toRevision, AuditOperation.MOD, metadata, changes);
    }

    private AuditRevisionDto mapRow(Object[] row) {
        AuditedEntityDescriptor descriptor = registry.require(row[0].toString());
        String entityId = row[1].toString();
        long revisionId = ((Number) row[2]).longValue();
        AuditOperation operation = operation(((Number) row[3]).intValue());
        Object id = parseId(descriptor, entityId);
        AuditReader reader = AuditReaderFactory.get(entityManager);

        Long previousRevision = previousRevision(descriptor, entityId, revisionId);
        Object previous = previousRevision == null
                ? null
                : reader.find(descriptor.entityClass(), id, previousRevision);
        Object current = operation == AuditOperation.DEL
                ? null
                : reader.find(descriptor.entityClass(), id, revisionId);
        List<FieldChangeDto> changes = mapper.compare(previous, current, descriptor);
        AuditRevisionEntity metadata = findRevisionMetadata(reader, revisionId);
        return dto(descriptor, entityId, revisionId, operation, metadata, changes);
    }

    private AuditRevisionDto dto(
            AuditedEntityDescriptor descriptor,
            String entityId,
            long revisionId,
            AuditOperation operation,
            AuditRevisionEntity metadata,
            List<FieldChangeDto> changes
    ) {
        ActorType actorType = metadata.getActorType() == null ? ActorType.UNKNOWN : metadata.getActorType();
        return new AuditRevisionDto(
                descriptor.displayName(),
                entityId,
                revisionId,
                Instant.ofEpochMilli(metadata.getTimestamp()),
                metadata.getUserId(),
                metadata.getUsername(),
                actorType,
                operation,
                serviceName,
                changes
        );
    }

    private AuditRevisionEntity findRevisionMetadata(AuditReader reader, long revisionId) {
        try {
            return reader.findRevision(AuditRevisionEntity.class, revisionId);
        } catch (RuntimeException exception) {
            throw new AuditRevisionNotFoundException("Métadonnées de révision introuvables : " + revisionId);
        }
    }

    private Long previousRevision(AuditedEntityDescriptor descriptor, String entityId, long revisionId) {
        Query query = entityManager.createNativeQuery(
                "SELECT MAX(rev) FROM " + descriptor.auditTable()
                        + " WHERE CAST(" + descriptor.idColumn() + " AS VARCHAR) = :entityId AND rev < :revisionId"
        );
        query.setParameter("entityId", entityId);
        query.setParameter("revisionId", revisionId);
        Object value = query.getSingleResult();
        return value == null ? null : ((Number) value).longValue();
    }

    private void ensureEntityRevisionExists(
            AuditedEntityDescriptor descriptor,
            String entityId,
            long revisionId
    ) {
        Query query = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM " + descriptor.auditTable()
                        + " WHERE CAST(" + descriptor.idColumn() + " AS VARCHAR) = :entityId AND rev = :revisionId"
        );
        query.setParameter("entityId", entityId);
        query.setParameter("revisionId", revisionId);
        if (((Number) query.getSingleResult()).longValue() == 0) {
            throw new AuditRevisionNotFoundException("La révision " + revisionId + " n'appartient pas à cette entité.");
        }
    }

    private String buildUnion(Collection<AuditedEntityDescriptor> descriptors, AuditFilter filter) {
        List<String> selections = new ArrayList<>();
        for (AuditedEntityDescriptor descriptor : descriptors) {
            StringBuilder sql = new StringBuilder()
                    .append("SELECT '").append(descriptor.apiName()).append("' AS entity_name, ")
                    .append("CAST(a.").append(descriptor.idColumn()).append(" AS VARCHAR) AS entity_id, ")
                    .append("a.rev AS revision_id, a.revtype AS revision_type ")
                    .append("FROM ").append(descriptor.auditTable()).append(" a ")
                    .append("JOIN revinfo r ON r.rev = a.rev WHERE 1=1");
            if (filter.entityId() != null) {
                sql.append(" AND CAST(a.").append(descriptor.idColumn()).append(" AS VARCHAR) = :entityId");
            }
            if (filter.revisionId() != null) {
                sql.append(" AND a.rev = :revisionId");
            }
            if (filter.userId() != null) {
                sql.append(" AND r.user_id = :userId");
            }
            if (filter.operation() != null) {
                sql.append(" AND a.revtype = :revisionType");
            }
            if (filter.startDate() != null) {
                sql.append(" AND r.revtstmp >= :startTimestamp");
            }
            if (filter.endDate() != null) {
                sql.append(" AND r.revtstmp < :endTimestamp");
            }
            selections.add(sql.toString());
        }
        return String.join(" UNION ALL ", selections);
    }

    private void bind(Query query, AuditFilter filter) {
        if (filter.entityId() != null) {
            query.setParameter("entityId", filter.entityId());
        }
        if (filter.revisionId() != null) {
            query.setParameter("revisionId", filter.revisionId());
        }
        if (filter.userId() != null) {
            query.setParameter("userId", filter.userId());
        }
        if (filter.operation() != null) {
            query.setParameter("revisionType", revisionType(filter.operation()));
        }
        if (filter.startDate() != null) {
            query.setParameter("startTimestamp", filter.startDate().toEpochMilli());
        }
        if (filter.endDate() != null) {
            query.setParameter("endTimestamp", filter.endDate().toEpochMilli());
        }
    }

    private void validateFilter(AuditFilter filter) {
        if (filter == null) {
            throw new AuditBadRequestException("Les critères d'audit sont requis.");
        }
        if (filter.entityId() != null && filter.entityType() == null) {
            throw new AuditBadRequestException("entityId exige entityType.");
        }
        if (filter.entityType() != null) {
            AuditedEntityDescriptor descriptor = registry.require(filter.entityType());
            if (filter.entityId() != null) {
                parseId(descriptor, filter.entityId());
            }
        }
        if (filter.startDate() != null && filter.endDate() != null
                && !filter.startDate().isBefore(filter.endDate())) {
            throw new AuditBadRequestException("L'intervalle de dates est invalide.");
        }
    }

    private Pageable validatePageable(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return Pageable.ofSize(20);
        }
        if (pageable.getPageSize() <= 0 || pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new AuditBadRequestException("La taille de page doit être comprise entre 1 et " + MAX_PAGE_SIZE + ".");
        }
        return pageable;
    }

    private Object parseId(AuditedEntityDescriptor descriptor, String entityId) {
        try {
            return descriptor.idParser().apply(entityId);
        } catch (RuntimeException exception) {
            throw new AuditBadRequestException("Identifiant invalide pour " + descriptor.apiName() + ".");
        }
    }

    private AuditOperation operation(int revisionType) {
        return switch (revisionType) {
            case 0 -> AuditOperation.ADD;
            case 1 -> AuditOperation.MOD;
            case 2 -> AuditOperation.DEL;
            default -> throw new IllegalStateException("Type de révision Envers inconnu : " + revisionType);
        };
    }

    private int revisionType(AuditOperation operation) {
        return switch (operation) {
            case ADD -> 0;
            case MOD -> 1;
            case DEL -> 2;
        };
    }
}
