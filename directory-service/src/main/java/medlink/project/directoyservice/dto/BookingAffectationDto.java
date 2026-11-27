package medlink.project.directoyservice.dto;

public record BookingAffectationDto(
        Long id,
        Long praticienId,
        Long cliniqueId,
        String cliniqueNom,
        Boolean actif,
        Boolean deleted) {
}
