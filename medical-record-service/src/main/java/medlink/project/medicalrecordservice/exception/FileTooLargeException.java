package medlink.project.medicalrecordservice.exception;

public class FileTooLargeException extends InvalidFileException {

    public FileTooLargeException(String message) {
        super(message);
    }
}
