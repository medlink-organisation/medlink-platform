package medlink.project.workflowservice.controller;

import medlink.project.workflowservice.exception.WorkflowNotFoundException;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.exception.WorkflowTaskNotFoundException;
import medlink.project.workflowservice.exception.WorkflowValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WorkflowExceptionHandler {

    @ExceptionHandler({WorkflowNotFoundException.class, WorkflowTaskNotFoundException.class})
    ProblemDetail notFound(RuntimeException exception) {
        return problem(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(WorkflowStateException.class)
    ProblemDetail conflict(WorkflowStateException exception) {
        return problem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({WorkflowValidationException.class, MethodArgumentNotValidException.class})
    ProblemDetail badRequest(Exception exception) {
        return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ProblemDetail problem(HttpStatus status, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(status.getReasonPhrase());
        return problem;
    }
}
