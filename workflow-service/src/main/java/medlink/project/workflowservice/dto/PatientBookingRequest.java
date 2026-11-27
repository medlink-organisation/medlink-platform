package medlink.project.workflowservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PatientBookingRequest(
        @Positive long slotId,
        @Size(max = 500) String reason,
        @NotBlank @Size(max = 100) String idempotencyKey) {
}
