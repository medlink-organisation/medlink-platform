package medlink.project.schedulingservice.dto;

import medlink.project.schedulingservice.entities.StatusRendezVous;

import java.time.LocalTime;
import java.util.UUID;

public record CreneauAppointmentSummaryDto(
        UUID id, LocalTime heureDebut, LocalTime heureFin, String patientId, Long cliniqueId, StatusRendezVous statut
) {}
