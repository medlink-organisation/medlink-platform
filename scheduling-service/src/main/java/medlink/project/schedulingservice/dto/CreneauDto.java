package medlink.project.schedulingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.validtaion.OnCreate;
import medlink.project.schedulingservice.validtaion.OnUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreneauDto {

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant est géré par le système"
    )
    private Long id;

    @NotNull(
            groups = OnCreate.class,
            message = "Le praticien est obligatoire"
    )
    @Positive(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant du praticien doit être positif"
    )
    private Long praticienId;

    @NotNull(
            groups = OnCreate.class,
            message = "La clinique est obligatoire"
    )
    @Positive(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant de la clinique doit être positif"
    )
    private Long cliniqueId;

    @NotNull(
            groups = OnCreate.class,
            message = "La date de début est obligatoire"
    )
    @Future(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de début doit être dans le futur"
    )
    private LocalDateTime dateDebut;

    @NotNull(
            groups = OnCreate.class,
            message = "La date de fin est obligatoire"
    )
    @Future(
            groups = {OnCreate.class, OnUpdate.class},
            message = "La date de fin doit être dans le futur"
    )
    private LocalDateTime dateFin;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le statut est géré par le système"
    )
    private StatutCreneau statut;

}
