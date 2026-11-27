package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;

import java.time.LocalDateTime;
import java.util.UUID;

public record PraticienPatientDto(
        String patientId,
        UUID lastRendezVousId,
        LocalDateTime lastConsultationAt,
        Long cliniqueId,
        StatusRendezVous status
) {
}
