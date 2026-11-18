package medlink.project.medicalrecordservice.controller;

import medlink.project.medicalrecordservice.exception.AntivirusUnavailableException;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.FileTooLargeException;
import medlink.project.medicalrecordservice.exception.InfectedFileException;
import medlink.project.medicalrecordservice.exception.InvalidFileException;
import medlink.project.medicalrecordservice.exception.ObjectStorageException;
import medlink.project.medicalrecordservice.exception.OrphanedObjectCleanupException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail notFound(ResourceNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    ProblemDetail conflict(ResourceAlreadyExistsException exception) {
        return problem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(FileTooLargeException.class)
    ProblemDetail tooLarge(FileTooLargeException exception) {
        return problem(HttpStatus.CONTENT_TOO_LARGE, exception.getMessage());
    }

    @ExceptionHandler(InfectedFileException.class)
    ProblemDetail infected(InfectedFileException exception) {
        return problem(HttpStatus.UNPROCESSABLE_CONTENT, exception.getMessage());
    }

    @ExceptionHandler({InvalidFileException.class, BusinessValidationException.class})
    ProblemDetail invalid(RuntimeException exception) {
        return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({AntivirusUnavailableException.class, ObjectStorageException.class})
    ProblemDetail unavailable(RuntimeException exception) {
        return problem(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @ExceptionHandler(OrphanedObjectCleanupException.class)
    ProblemDetail cleanupFailure(OrphanedObjectCleanupException exception) {
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ProblemDetail forbidden(AccessDeniedException exception) {
        return problem(HttpStatus.FORBIDDEN, "Accès refusé.");
    }

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail unauthorized(AuthenticationException exception) {
        return problem(HttpStatus.UNAUTHORIZED, "Authentification requise.");
    }

    private ProblemDetail problem(HttpStatus status, String detail) {
        return ProblemDetail.forStatusAndDetail(status, detail);
    }
}
