package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;

import java.time.LocalDateTime;
import java.util.UUID;

public record PatientRendezVousDto(
        UUID id,
        Long creneauId,
        Long praticienId,
        Long cliniqueId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        StatusRendezVous status,
        TypeRendezVous type
) {
}
