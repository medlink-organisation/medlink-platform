package medlink.project.directoyservice.exception;

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

    @ExceptionHandler(EntityNotAuditableException.class)
    ProblemDetail forbiddenEntity(EntityNotAuditableException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AuditRevisionNotFoundException.class)
    ProblemDetail missingRevision(AuditRevisionNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
