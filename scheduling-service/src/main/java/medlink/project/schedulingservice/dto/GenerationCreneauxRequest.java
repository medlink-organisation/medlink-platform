package medlink.project.schedulingservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record GenerationCreneauxRequest(
        @NotNull @Positive Long praticienId,
        @NotNull @Positive Long cliniqueId,
        @NotEmpty List<@NotNull LocalDate> dates,
        @NotNull LocalTime heureDebut,
        @NotNull LocalTime heureFin,
        @Min(5) @Max(480) int dureeCreneauMinutes
) {
}
