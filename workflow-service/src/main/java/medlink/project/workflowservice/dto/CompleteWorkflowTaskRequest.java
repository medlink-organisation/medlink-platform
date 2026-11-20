package medlink.project.workflowservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CompleteWorkflowTaskRequest(
        @NotNull Map<String, Object> variables) {
}
