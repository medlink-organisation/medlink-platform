package medlink.project.schedulingservice.dto;

import java.time.LocalTime;

public record CreneauDayRangeDto(LocalTime heureDebut, LocalTime heureFin, Long cliniqueId) {}
