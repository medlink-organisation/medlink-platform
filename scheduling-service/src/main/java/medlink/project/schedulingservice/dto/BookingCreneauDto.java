package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatutCreneau;
import java.time.LocalDateTime;

public record BookingCreneauDto(
        Long id,
        Long praticienId,
        Long cliniqueId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        StatutCreneau statut) {
}
