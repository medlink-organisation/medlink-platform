package medlink.project.workflowservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record StartWorkflowRequest(
        @Positive long patientId,
        @Positive long praticienId,
        @Positive long cliniqueId,
        @NotBlank String idempotencyKey) {
}
