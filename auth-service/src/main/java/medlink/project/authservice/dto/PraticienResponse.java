package medlink.project.authservice.dto;


public record PraticienResponse(
        Long id,
        String keycloakId,
        String nom,
        String prenom,
        String email,
        String phone,
        String numeroOrdre,
        Long specialiteId,
        Boolean actif
) {}
