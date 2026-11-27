package medlink.project.auditcore.model;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public record AuditedEntityDescriptor(
        String apiName,
        String displayName,
        Class<?> entityClass,
        String auditTable,
        String idColumn,
        Function<String, Object> idParser,
        Set<String> exposedFields
) {
    public AuditedEntityDescriptor {
        apiName = apiName.toLowerCase(Locale.ROOT);
        exposedFields = Set.copyOf(new LinkedHashSet<>(exposedFields));
    }

    public static Function<String, Object> longId() {
        return value -> Long.valueOf(value);
    }

    public static Function<String, Object> uuidId() {
        return UUID::fromString;
    }
}
