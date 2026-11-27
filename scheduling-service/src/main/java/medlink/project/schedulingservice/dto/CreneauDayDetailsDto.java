package medlink.project.schedulingservice.dto;

import java.util.List;

public record CreneauDayDetailsDto(CreneauDaySummaryDto resume, List<CreneauAppointmentSummaryDto> rendezVous) {}
