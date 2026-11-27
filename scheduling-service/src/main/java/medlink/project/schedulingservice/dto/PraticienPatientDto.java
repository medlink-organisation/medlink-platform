package medlink.project.schedulingservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PraticienPatientDto(
        Long patientId,
        UUID lastRendezVousId,
        LocalDateTime lastConsultationAt,
        Long cliniqueId
) {
}
