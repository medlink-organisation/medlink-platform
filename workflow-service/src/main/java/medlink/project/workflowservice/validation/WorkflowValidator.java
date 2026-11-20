package medlink.project.workflowservice.validation;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.exception.WorkflowNotFoundException;
import medlink.project.workflowservice.exception.WorkflowStateException;
import medlink.project.workflowservice.exception.WorkflowTaskNotFoundException;
import medlink.project.workflowservice.exception.WorkflowValidationException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.eventsubscription.api.EventSubscription;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkflowValidator {

    private static final String CHOOSE_SLOT_TASK =
            "sid-09060B9A-FBC3-487C-8CA0-ADD7F9A2D40A";
    private static final String PRESENCE_TASK =
            "sid-A9B6D073-5318-4305-976A-0EED79871E79";
    private static final String COMPLETE_CONSULTATION_TASK =
            "sid-67B03EB7-8B5A-44E7-9A78-55F7CE14FDAB";
    private static final String FOLLOW_UP_TASK =
            "sid-93365DB8-C583-43C8-9A1E-0AAE5A1189F3";

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public void validateStart(
            long patientId,
            long praticienId,
            long cliniqueId,
            String actorUserId,
            String idempotencyKey) {
        requirePositive(patientId, "patientId");
        requirePositive(praticienId, "praticienId");
        requirePositive(cliniqueId, "cliniqueId");
        requireText(actorUserId, "actorUserId");
        requireText(idempotencyKey, "idempotencyKey");
    }

    public void requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new WorkflowValidationException(name + " obligatoire");
        }
    }

    public void requireActiveProcess(String processInstanceId) {
        requireText(processInstanceId, "processInstanceId");
        if (runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult() == null) {
            throw new WorkflowNotFoundException(processInstanceId);
        }
    }

    public HistoricProcessInstance requireHistoricProcess(
            HistoricProcessInstance process,
            String processInstanceId) {
        if (process == null) {
            throw new WorkflowNotFoundException(processInstanceId);
        }
        return process;
    }

    public Task requireActiveTask(String processInstanceId, String taskId) {
        requireActiveProcess(processInstanceId);
        requireText(taskId, "taskId");
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .processInstanceId(processInstanceId)
                .active()
                .singleResult();
        if (task == null) {
            throw new WorkflowTaskNotFoundException(taskId);
        }
        return task;
    }

    public EventSubscription requireMessageSubscription(
            String processInstanceId,
            String messageName) {
        requireActiveProcess(processInstanceId);
        EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
                .processInstanceId(processInstanceId)
                .eventType("message")
                .eventName(messageName)
                .singleResult();
        if (subscription == null) {
            throw new WorkflowStateException(
                    "Le processus n'attend pas le message " + messageName);
        }
        return subscription;
    }

    public Map<String, Object> validateTaskVariables(
            String definitionKey,
            Map<String, Object> variables) {
        Map<String, Object> source = variables == null ? Map.of() : variables;
        Map<String, Object> result = new HashMap<>();
        switch (definitionKey) {
            case CHOOSE_SLOT_TASK ->
                    result.put("creneauId", positiveNumber(source, "creneauId"));
            case PRESENCE_TASK ->
                    result.put("patientPresent", booleanValue(source, "patientPresent"));
            case COMPLETE_CONSULTATION_TASK ->
                    result.put("controleRequis", booleanValue(source, "controleRequis"));
            case FOLLOW_UP_TASK -> validateFollowUpVariables(source, result);
            default -> throw new WorkflowStateException(
                    "Type de tâche non pris en charge : " + definitionKey);
        }
        return Map.copyOf(result);
    }

    public Optional<String> optionalText(String value) {
        return Optional.ofNullable(value).filter(text -> !text.isBlank());
    }

    private void validateFollowUpVariables(
            Map<String, Object> source,
            Map<String, Object> result) {
        result.put("creneauId", positiveNumber(source, "creneauId"));
        Object motif = source.get("motifControle");
        if (motif instanceof String text && !text.isBlank()) {
            result.put("motifControle", text);
        }
        if (source.containsKey("dateControle")) {
            result.put("dateControle", source.get("dateControle"));
        }
    }

    private void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new WorkflowValidationException(name + " doit être positif");
        }
    }

    private long positiveNumber(Map<String, Object> variables, String name) {
        Object value = variables.get(name);
        if (!(value instanceof Number number) || number.longValue() <= 0) {
            throw new WorkflowValidationException(name + " doit être positif");
        }
        return number.longValue();
    }

    private boolean booleanValue(Map<String, Object> variables, String name) {
        Object value = variables.get(name);
        if (!(value instanceof Boolean bool)) {
            throw new WorkflowValidationException(name + " doit être booléen");
        }
        return bool;
    }
}
