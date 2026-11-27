package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;

import java.time.LocalDateTime;
import java.util.UUID;

public record PraticienPatientDto(
        String patientId,
        UUID lastRendezVousId,
        LocalDateTime lastRendezVousAt,
        Long cliniqueId,
        StatusRendezVous status
) {
}