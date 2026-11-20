package medlink.project.workflowservice.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkflowEventRequest(
        @NotBlank String rendezVousId,
        @NotBlank String eventId,
        String reason) {
}
