package medlink.project.schedulingservice.dto;

import java.time.LocalDateTime;

public record BookingCreneauDto(
        Long id,
        Long praticienId,
        Long cliniqueId,
        LocalDateTime dateDebut,
        LocalDateTime dateFin) {
}
