package medlink.project.medicalrecordservice.exception;

public class AntivirusUnavailableException extends RuntimeException {

    public AntivirusUnavailableException(String message) {
        super(message);
    }

    public AntivirusUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
