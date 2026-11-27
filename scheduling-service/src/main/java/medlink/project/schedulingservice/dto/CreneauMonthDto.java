package medlink.project.schedulingservice.dto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record CreneauMonthDto(
        Long praticienId,
        YearMonth mois,
        LocalDate dateDebutActivite,
        LocalDate dateFinActivite,
        List<CreneauDaySummaryDto> jours
) {}
