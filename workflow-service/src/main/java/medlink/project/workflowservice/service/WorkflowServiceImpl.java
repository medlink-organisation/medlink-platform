package medlink.project.workflowservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.validation.WorkflowValidator;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.eventsubscription.api.EventSubscription;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkflowServiceImpl implements WorkflowService {

    private static final String PROCESS_KEY = "medilinkprocess";
    private static final String CANCEL_MESSAGE = "annulation de rendez-vous";
    private static final String RESCHEDULE_MESSAGE = "changement de rendez-vous";

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final WorkflowValidator validator;

    @Override
    @Transactional
    public WorkflowInstance start(
            long patientId,
            long praticienId,
            long cliniqueId,
            String actorUserId,
            String idempotencyKey) {

        validator.validateStart(
                patientId,
                praticienId,
                cliniqueId,
                actorUserId,
                idempotencyKey
        );

        ProcessInstance active = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(idempotencyKey)
                .singleResult();
        if (active != null) {
            return new WorkflowInstance(active.getId(), active.getBusinessKey());
        }

        HistoricProcessInstance completed = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(idempotencyKey)
                .singleResult();
        if (completed != null) {
            return new WorkflowInstance(completed.getId(), completed.getBusinessKey());
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("patientId", patientId);
        variables.put("praticienId", praticienId);
        variables.put("cliniqueId", cliniqueId);
        variables.put("actorUserId", actorUserId);
        variables.put("typeRdv", "NORMAL");

        ProcessInstance process = runtimeService.startProcessInstanceByKey(
                PROCESS_KEY,
                idempotencyKey,
                variables
        );
        return new WorkflowInstance(process.getId(), process.getBusinessKey());
    }

    @Override
    public WorkflowStatus getStatus(String processInstanceId) {
        validator.requireText(processInstanceId, "processInstanceId");
        ProcessInstance process = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (process != null) {
            List<String> activities = runtimeService.getActiveActivityIds(processInstanceId);
            Object rendezVousId = runtimeService.getVariable(processInstanceId, "rdvId");
            return new WorkflowStatus(
                    processInstanceId,
                    true,
                    activities.isEmpty() ? "" : activities.getFirst(),
                    rendezVousId instanceof String id ? id : ""
            );
        }

        HistoricProcessInstance historic = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        historic = validator.requireHistoricProcess(historic, processInstanceId);
        Object rendezVousId = historic.getProcessVariables().get("rdvId");
        return new WorkflowStatus(
                processInstanceId,
                false,
                "",
                rendezVousId instanceof String id ? id : ""
        );
    }

    @Override
    public List<WorkflowTask> getTasks(String processInstanceId) {
        validator.requireActiveProcess(processInstanceId);
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list()
                .stream()
                .map(this::toTask)
                .toList();
    }

    @Override
    @Transactional
    public WorkflowStatus completeTask(
            String processInstanceId,
            String taskId,
            Map<String, Object> variables) {

        Task task = validator.requireActiveTask(processInstanceId, taskId);
        Map<String, Object> safeVariables = validator.validateTaskVariables(
                task.getTaskDefinitionKey(),
                variables
        );
        taskService.complete(taskId, safeVariables);
        return getStatus(processInstanceId);
    }

    @Override
    @Transactional
    public WorkflowEvent cancel(
            String processInstanceId,
            String rendezVousId,
            String reason,
            String eventId) {
        return signal(processInstanceId, rendezVousId, reason, eventId, CANCEL_MESSAGE);
    }

    @Override
    @Transactional
    public WorkflowEvent reschedule(
            String processInstanceId,
            String rendezVousId,
            String eventId) {
        return signal(processInstanceId, rendezVousId, "", eventId, RESCHEDULE_MESSAGE);
    }

    private WorkflowEvent signal(
            String processInstanceId,
            String rendezVousId,
            String reason,
            String eventId,
            String messageName) {

        validator.requireText(rendezVousId, "rendezVousId");
        validator.requireText(eventId, "eventId");
        EventSubscription subscription = validator.requireMessageSubscription(
                processInstanceId,
                messageName
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("rdvId", rendezVousId);
        variables.put("eventId", eventId);
        validator.optionalText(reason)
                .ifPresent(value -> variables.put("annulationMotif", value));
        runtimeService.messageEventReceived(
                messageName,
                subscription.getExecutionId(),
                variables
        );
        return new WorkflowEvent(true, processInstanceId, Instant.now());
    }

    private WorkflowTask toTask(Task task) {
        return new WorkflowTask(
                task.getId(),
                task.getTaskDefinitionKey(),
                task.getName(),
                task.getAssignee(),
                task.getCreateTime().toInstant()
        );
    }

}
