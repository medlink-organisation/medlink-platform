package medlink.project.medicalrecordservice.exception;

public class OrphanedObjectCleanupException extends RuntimeException {

    public OrphanedObjectCleanupException(String message, Throwable cause) {
        super(message, cause);
    }
}
