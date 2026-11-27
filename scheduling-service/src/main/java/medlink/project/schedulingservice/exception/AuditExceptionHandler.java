package medlink.project.schedulingservice.exception;

import medlink.project.auditcore.exception.AuditBadRequestException;
import medlink.project.auditcore.exception.AuditRevisionNotFoundException;
import medlink.project.auditcore.exception.EntityNotAuditableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuditExceptionHandler {

    @ExceptionHandler(AuditBadRequestException.class)
    ProblemDetail badRequest(AuditBadRequestException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({EntityNotAuditableException.class, AuditRevisionNotFoundException.class})
    ProblemDetail notFound(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
