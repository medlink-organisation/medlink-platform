package medlink.project.auditcore.exception;

public class AuditBadRequestException extends RuntimeException {
    public AuditBadRequestException(String message) {
        super(message);
    }
}
