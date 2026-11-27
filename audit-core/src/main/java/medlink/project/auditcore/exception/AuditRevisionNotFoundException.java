package medlink.project.auditcore.exception;

public class AuditRevisionNotFoundException extends RuntimeException {
    public AuditRevisionNotFoundException(String message) {
        super(message);
    }
}
