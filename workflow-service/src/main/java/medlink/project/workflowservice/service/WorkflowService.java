package medlink.project.workflowservice.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface WorkflowService {

    WorkflowInstance start(
            long patientId,
            long praticienId,
            long cliniqueId,
            String actorUserId,
            String idempotencyKey);

    WorkflowStatus getStatus(String processInstanceId);

    List<WorkflowTask> getTasks(String processInstanceId);

    WorkflowStatus completeTask(
            String processInstanceId,
            String taskId,
            Map<String, Object> variables);

    WorkflowEvent cancel(
            String processInstanceId,
            String rendezVousId,
            String reason,
            String eventId);

    WorkflowEvent reschedule(
            String processInstanceId,
            String rendezVousId,
            String eventId);

    record WorkflowInstance(String processInstanceId, String businessKey) {
    }

    record WorkflowStatus(
            String processInstanceId,
            boolean active,
            String currentActivityId,
            String rendezVousId) {
    }

    record WorkflowTask(
            String id,
            String definitionKey,
            String name,
            String assignee,
            Instant createdAt) {
    }

    record WorkflowEvent(
            boolean accepted,
            String processInstanceId,
            Instant receivedAt) {
    }
}
