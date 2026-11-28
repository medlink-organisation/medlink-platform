package medlink.project.medicalrecordservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medlink.project.medicalrecordservice.validation.OnCreate;
import medlink.project.medicalrecordservice.validation.OnUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompteRenduDto {

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

    @NotNull(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du rendez-vous est obligatoire"
    )
    private UUID rendezVousId;

    @NotNull(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du praticien est obligatoire"
    )
    private Long praticienId;

    private Long cliniqueId;

    private LocalDateTime dateConsultation;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de rédaction est gérée par le système"
    )
    private LocalDateTime dateRedaction;

    @NotBlank(
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le contenu est obligatoire"
    )
    private String contenu;

    private String motif;
    private String symptomes;
    private String diagnostic;
    private String observations;
    private String traitementRecommande;
    private String examensComplementaires;
    private String recommandations;
    private Boolean controleRequis;
    private String praticienNom;
    private String praticienPrenom;
    private String specialiteNom;
    private String cliniqueNom;
}
