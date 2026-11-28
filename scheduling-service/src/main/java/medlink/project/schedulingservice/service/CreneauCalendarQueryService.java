package medlink.project.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.*;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreneauCalendarQueryService {
    private final CreneauRepository creneauRepository;
    private final RendezVousRepository rendezVousRepository;

    public CreneauMonthDto getMonth(Long praticienId, YearMonth month) {
        LocalDate activityStart = activityStart(praticienId);
        LocalDate activityEnd = activityEnd(praticienId);
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        if (end.isBefore(activityStart)) {
            throw new BusinessException("Le mois demandé précède la date de début de travail du praticien.");
        }
        Map<LocalDate, List<Creneau>> slots = creneauRepository
                .findCalendar(praticienId, null, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream()
                .collect(Collectors.groupingBy(slot -> slot.getDateDebut().toLocalDate()));
        List<CreneauDaySummaryDto> days = start.datesUntil(end.plusDays(1))
                .map(date -> summarize(date, activityStart, activityEnd, slots.getOrDefault(date, List.of())))
                .toList();
        return new CreneauMonthDto(praticienId, month, activityStart, activityEnd, days);
    }

    public CreneauDayDetailsDto getDay(Long praticienId, LocalDate date) {
        LocalDate activityStart = activityStart(praticienId);
        LocalDate activityEnd = activityEnd(praticienId);
        List<Creneau> slots = creneauRepository.findCalendar(
                praticienId, null, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        CreneauDaySummaryDto summary = summarize(date, activityStart, activityEnd, slots);
        List<CreneauAppointmentSummaryDto> appointments = rendezVousRepository
                .findAgendaByPraticien(praticienId, date.atStartOfDay(), date.plusDays(1).atStartOfDay()).stream()
                .map(rendezVous -> new CreneauAppointmentSummaryDto(
                        rendezVous.getId(), rendezVous.getCreneau().getDateDebut().toLocalTime(),
                        rendezVous.getCreneau().getDateFin().toLocalTime(), rendezVous.getPatientId().toString(),
                        rendezVous.getCreneau().getCliniqueId(), rendezVous.getStatus()))
                .toList();
        return new CreneauDayDetailsDto(summary, appointments);
    }

    private CreneauDaySummaryDto summarize(LocalDate date, LocalDate activityStart, LocalDate activityEnd, List<Creneau> slots) {
        String planningStatus;
        if (date.isBefore(activityStart)) planningStatus = "HORS_PERIODE_ACTIVITE";
        else if (slots.isEmpty()) planningStatus = "NON_CONFIGURE";
        else if (slots.stream().allMatch(slot -> slot.getStatut() == StatutCreneau.BLOQUE
                || slot.getStatut() == StatutCreneau.INDISPONIBLE)) planningStatus = "INDISPONIBLE";
        else planningStatus = "TRAVAIL";

        int available = count(slots, StatutCreneau.DISPONIBLE);
        int reserved = count(slots, StatutCreneau.RESERVE);
        int blocked = count(slots, StatutCreneau.BLOQUE);
        String reservationStatus = slots.isEmpty() ? "AUCUN_CRENEAU"
                : available > 0 ? "DISPONIBLE" : reserved > 0 ? "COMPLET" : "INDISPONIBLE";
        return new CreneauDaySummaryDto(date, planningStatus, reservationStatus, null, mergeRanges(slots),
                slots.size(), available, reserved, blocked);
    }

    private List<CreneauDayRangeDto> mergeRanges(List<Creneau> slots) {
        List<Creneau> ordered = slots.stream().sorted(Comparator.comparing(Creneau::getDateDebut)).toList();
        List<MutableRange> ranges = new ArrayList<>();
        for (Creneau slot : ordered) {
            LocalTime start = slot.getDateDebut().toLocalTime();
            LocalTime end = slot.getDateFin().toLocalTime();
            MutableRange previous = ranges.isEmpty() ? null : ranges.get(ranges.size() - 1);
            if (previous != null && previous.clinicId.equals(slot.getCliniqueId()) && previous.end.equals(start)) {
                previous.end = end;
            } else {
                ranges.add(new MutableRange(start, end, slot.getCliniqueId()));
            }
        }
        return ranges.stream().map(range -> new CreneauDayRangeDto(range.start, range.end, range.clinicId)).toList();
    }

    private int count(List<Creneau> slots, StatutCreneau status) {
        return (int) slots.stream().filter(slot -> slot.getStatut() == status).count();
    }

    private LocalDate activityStart(Long praticienId) {
        return creneauRepository.findActivityStart(praticienId).map(value -> value.toLocalDate())
                .orElse(LocalDate.now());
    }

    private LocalDate activityEnd(Long praticienId) {
        return creneauRepository.findActivityEnd(praticienId).map(value -> value.toLocalDate())
                .orElse(null);
    }

    private static final class MutableRange {
        private final LocalTime start;
        private LocalTime end;
        private final Long clinicId;

        private MutableRange(LocalTime start, LocalTime end, Long clinicId) {
            this.start = start;
            this.end = end;
            this.clinicId = clinicId;
        }
    }
}
