package medlink.project.praticienservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import medlink.project.praticienservice.validtaion.OnCreate;
import medlink.project.praticienservice.validtaion.OnUpdate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PraticienDto {

    private Long id;

    @NotBlank(message = "KeycloakId obligatoire",
            groups = OnCreate.class)
    private String keycloakId;

    @NotBlank(message = "Nom obligatoire",
            groups = OnCreate.class)
    @Pattern(
            regexp = ".*\\S.*",
            message = "Nom ne doit pas être vide",
            groups = OnUpdate.class
    )
    private String nom;


    @NotBlank(
            message = "Prénom obligatoire",
            groups = OnCreate.class
    )
    @Pattern(
            regexp = ".*\\S.*",
            message = "Prénom ne doit pas être vide",
            groups = OnUpdate.class
    )
    private String prenom;

    @NotBlank(
            message = "Email obligatoire",
            groups = OnCreate.class
    )
    @Email(
            message = "Email invalide",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String email;


    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "Téléphone invalide",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String phone;

    private Boolean actif;

    private String numeroOrdre;

    @NotNull(
            message = "Spécialité obligatoire",
            groups = OnCreate.class
    )
    private Long specialiteId;
}
