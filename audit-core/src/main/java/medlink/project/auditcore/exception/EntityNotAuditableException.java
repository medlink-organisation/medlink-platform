package medlink.project.auditcore.exception;

public class EntityNotAuditableException extends RuntimeException {
    public EntityNotAuditableException(String message) {
        super(message);
    }
}
