package medlink.project.medicalrecordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medlink.project.medicalrecordservice.validation.OnCreate;
import medlink.project.medicalrecordservice.validation.OnUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultatExamenDto {

    @Null(groups = OnCreate.class, message = "L'identifiant est géré par le système")
    @NotNull(groups = OnUpdate.class, message = "L'identifiant est obligatoire lors d'une modification")
    private UUID id;

    @NotNull(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du dossier médical est obligatoire"
    )
    @Positive(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du dossier médical doit être positif"
    )
    private Long dossierMedicalId;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le document médical est géré par le système"
    )
    private Long documentMedicalId;

    @NotBlank(
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le type d'examen est obligatoire"
    )
    @Size(
            max = 150,
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le type d'examen ne doit pas dépasser 150 caractères"
    )
    private String typeExamen;

    @NotNull(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de l'examen est obligatoire"
    )
    @PastOrPresent(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de l'examen ne peut pas être future"
    )
    private LocalDate dateExamen;

    private String conclusion;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de publication est gérée par le système"
    )
    private LocalDateTime datePublication;
    private String nomFichier;
    private Long tailleOctets;
    private String praticienNomComplet;
}
