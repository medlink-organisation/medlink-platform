package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminRendezVousDto(
        UUID id,
        String patientId,
        String patientNom,
        String patientPrenom,
        Long praticienId,
        String praticienNom,
        String praticienPrenom,
        Long creneauId,
        Long cliniqueId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        TypeRendezVous type,
        StatusRendezVous status
) {
}
