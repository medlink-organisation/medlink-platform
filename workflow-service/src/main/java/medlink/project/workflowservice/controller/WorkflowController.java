package medlink.project.workflowservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.dto.CompleteWorkflowTaskRequest;
import medlink.project.workflowservice.dto.StartWorkflowRequest;
import medlink.project.workflowservice.dto.WorkflowEventRequest;
import medlink.project.workflowservice.client.PraticienGrpcClient;
import medlink.project.workflowservice.service.WorkflowService;
import medlink.project.workflowservice.service.WorkflowService.WorkflowEvent;
import medlink.project.workflowservice.service.WorkflowService.WorkflowInstance;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import medlink.project.workflowservice.service.WorkflowService.WorkflowTask;
import medlink.project.workflowservice.service.WorkflowService.PraticienWorkflowTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;
    private final PraticienGrpcClient praticienGrpcClient;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkflowInstance> start(
            @Valid @RequestBody StartWorkflowRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        WorkflowInstance workflow = workflowService.start(
                request.patientId(),
                request.praticienId(),
                request.cliniqueId(),
                jwt.getSubject(),
                request.idempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(workflow);
    }

    @GetMapping("/{processInstanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkflowStatus> getStatus(
            @PathVariable String processInstanceId) {
        return ResponseEntity.ok(workflowService.getStatus(processInstanceId));
    }

    @GetMapping("/{processInstanceId}/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WorkflowTask>> getTasks(
            @PathVariable String processInstanceId) {
        return ResponseEntity.ok(workflowService.getTasks(processInstanceId));
    }

    @PostMapping("/{processInstanceId}/tasks/{taskId}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkflowStatus> completeTask(
            @PathVariable String processInstanceId,
            @PathVariable String taskId,
            @Valid @RequestBody CompleteWorkflowTaskRequest request) {

        return ResponseEntity.ok(workflowService.completeTask(
                processInstanceId,
                taskId,
                request.variables()
        ));
    }

    @GetMapping("/tasks/me")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<List<PraticienWorkflowTask>> getMyTasks(
            @AuthenticationPrincipal Jwt jwt) {
        long praticienId = praticienGrpcClient
                .getByKeycloakId(jwt.getSubject())
                .getId();
        return ResponseEntity.ok(workflowService.getTasksForPraticien(praticienId));
    }

    @PostMapping("/{processInstanceId}/tasks/{taskId}/complete-mine")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<WorkflowStatus> completeMyTask(
            @PathVariable String processInstanceId,
            @PathVariable String taskId,
            @Valid @RequestBody CompleteWorkflowTaskRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        long praticienId = praticienGrpcClient
                .getByKeycloakId(jwt.getSubject())
                .getId();
        return ResponseEntity.ok(workflowService.completeTaskForPraticien(
                processInstanceId,
                taskId,
                request.variables(),
                praticienId
        ));
    }

    @PostMapping("/{processInstanceId}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkflowEvent> cancel(
            @PathVariable String processInstanceId,
            @Valid @RequestBody WorkflowEventRequest request) {

        return ResponseEntity.ok(workflowService.cancel(
                processInstanceId,
                request.rendezVousId(),
                request.reason(),
                request.eventId()
        ));
    }

    @PostMapping("/{processInstanceId}/reschedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkflowEvent> reschedule(
            @PathVariable String processInstanceId,
            @Valid @RequestBody WorkflowEventRequest request) {

        return ResponseEntity.ok(workflowService.reschedule(
                processInstanceId,
                request.rendezVousId(),
                request.eventId()
        ));
    }
}
