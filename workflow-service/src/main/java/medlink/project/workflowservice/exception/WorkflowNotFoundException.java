package medlink.project.workflowservice.exception;

public class WorkflowNotFoundException extends RuntimeException {

    public WorkflowNotFoundException(String processInstanceId) {
        super("Workflow introuvable : " + processInstanceId);
    }
}
