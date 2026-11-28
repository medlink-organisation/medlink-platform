package medlink.project.schedulingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;

import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.TypeRendezVous;
import medlink.project.schedulingservice.validtaion.OnCreate;
import medlink.project.schedulingservice.validtaion.OnUpdate;

import java.util.UUID;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousDto {

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "L'identifiant est géré par le système"
    )
    private UUID id;

    @NotNull(
            groups = OnCreate.class,
            message = "Le créneau est obligatoire"
    )
    @Positive(
            groups = OnCreate.class,
            message = "L'identifiant du créneau doit être positif"
    )
    @Null(
            groups = OnUpdate.class,
            message = "Le créneau ne peut pas être modifié avec cette opération"
    )
    private Long creneauId;

    @NotNull(
            groups = OnCreate.class,
            message = "Le patient est obligatoire"
    )
    @Positive(
            groups = OnCreate.class,
            message = "L'identifiant du patient doit être positif"
    )
    @Null(
            groups = OnUpdate.class,
            message = "Le patient ne peut pas être modifié"
    )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patientId;

    @Null(
            groups = {OnCreate.class, OnUpdate.class},
            message = "Le statut est géré par le système"
    )
    private StatusRendezVous status;

    @NotNull(
            groups = OnCreate.class,
            message = "Le type de rendez-vous est obligatoire"
    )
    private TypeRendezVous type;
}
