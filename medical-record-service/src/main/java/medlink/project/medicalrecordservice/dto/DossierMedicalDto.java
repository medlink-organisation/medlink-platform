package medlink.project.medicalrecordservice.dto;

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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedicalDto {

    @Null(groups = OnCreate.class, message = "L'identifiant est géré par le système")
    @NotNull(groups = OnUpdate.class, message = "L'identifiant est obligatoire lors d'une modification")
    private Long id;

    @NotNull(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du patient est obligatoire"
    )
    @Positive(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du patient doit être positif"
    )
    private Long patientId;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de création est gérée par le système"
    )
    private LocalDateTime dateCreation;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de dernière modification est gérée par le système"
    )
    private LocalDateTime derniereModification;
}
