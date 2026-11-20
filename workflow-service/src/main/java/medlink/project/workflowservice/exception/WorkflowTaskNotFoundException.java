package medlink.project.workflowservice.exception;

public class WorkflowTaskNotFoundException extends RuntimeException {

    public WorkflowTaskNotFoundException(String taskId) {
        super("Tâche workflow introuvable : " + taskId);
    }
}
