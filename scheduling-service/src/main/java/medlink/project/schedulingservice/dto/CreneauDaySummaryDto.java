package medlink.project.schedulingservice.dto;

import java.time.LocalDate;
import java.util.List;

public record CreneauDaySummaryDto(
        LocalDate date,
        String statutPlanning,
        String statutReservation,
        String motif,
        List<CreneauDayRangeDto> plages,
        int totalCreneaux,
        int creneauxDisponibles,
        int rendezVousReserves,
        int creneauxBloques
) {}
