package medlink.project.auditcore.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import medlink.project.auditcore.dto.FieldChangeDto;
import medlink.project.auditcore.model.AuditedEntityDescriptor;

import java.lang.reflect.Field;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AuditMapper {

    private final PersistenceUnitUtil persistenceUnitUtil;

    public AuditMapper(EntityManager entityManager) {
        this.persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }

    public List<FieldChangeDto> compare(
            Object before,
            Object after,
            AuditedEntityDescriptor descriptor
    ) {
        List<FieldChangeDto> changes = new ArrayList<>();
        for (String fieldName : descriptor.exposedFields()) {
            Object oldValue = externalValue(readField(before, fieldName));
            Object newValue = externalValue(readField(after, fieldName));
            if (!Objects.equals(oldValue, newValue)) {
                changes.add(new FieldChangeDto(fieldName, oldValue, newValue));
            }
        }
        return List.copyOf(changes);
    }

    private Object readField(Object source, String fieldName) {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        while (type != null && type != Object.class) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(source);
            } catch (NoSuchFieldException ignored) {
                type = type.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Impossible de lire le champ audité " + fieldName, exception);
            }
        }
        throw new IllegalStateException("Champ absent de la liste blanche : " + fieldName);
    }

    private Object externalValue(Object value) {
        if (value == null || value instanceof String || value instanceof Number
                || value instanceof Boolean || value instanceof Enum<?>
                || value instanceof TemporalAccessor || value instanceof UUID) {
            return value;
        }
        if (value instanceof Collection<?>) {
            return null;
        }
        try {
            Object identifier = persistenceUnitUtil.getIdentifier(value);
            return identifier == null ? null : identifier.toString();
        } catch (IllegalArgumentException ignored) {
            return value.toString();
        }
    }
}
