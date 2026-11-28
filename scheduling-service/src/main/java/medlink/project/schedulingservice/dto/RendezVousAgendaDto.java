package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;

import java.time.LocalDateTime;
import java.util.UUID;

public record RendezVousAgendaDto(
        UUID id,
        String patientId,
        String patientNom,
        String patientPrenom,
        Long praticienId,
        Long cliniqueId,
        Long creneauId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        StatusRendezVous status,
        TypeRendezVous type
) {
}
