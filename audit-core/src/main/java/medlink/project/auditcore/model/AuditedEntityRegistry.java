package medlink.project.auditcore.model;

import medlink.project.auditcore.exception.AuditBadRequestException;
import medlink.project.auditcore.exception.EntityNotAuditableException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class AuditedEntityRegistry {

    private final Map<String, AuditedEntityDescriptor> byApiName;

    public AuditedEntityRegistry(Collection<AuditedEntityDescriptor> descriptors) {
        Map<String, AuditedEntityDescriptor> values = new LinkedHashMap<>();
        descriptors.forEach(descriptor -> values.put(descriptor.apiName(), descriptor));
        this.byApiName = Map.copyOf(values);
    }

    public Collection<AuditedEntityDescriptor> all() {
        return byApiName.values();
    }

    public AuditedEntityDescriptor require(String entityType) {
        if (entityType == null || entityType.isBlank()) {
            throw new AuditBadRequestException("Le type d'entité est requis.");
        }
        AuditedEntityDescriptor descriptor = byApiName.get(entityType.toLowerCase(Locale.ROOT));
        if (descriptor == null) {
            throw new EntityNotAuditableException("Type d'entité non auditable ou non autorisé : " + entityType);
        }
        return descriptor;
    }
}
