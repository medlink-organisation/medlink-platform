package medlink.project.auditcore.dto;

public record FieldChangeDto(
        String fieldName,
        Object oldValue,
        Object newValue
) {
}
