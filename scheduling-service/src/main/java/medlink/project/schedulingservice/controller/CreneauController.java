package medlink.project.schedulingservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.BookingCreneauDto;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.dto.GenerationCreneauxRequest;
import medlink.project.schedulingservice.dto.GenerationCreneauxResponse;
import medlink.project.schedulingservice.dto.CreneauMonthDto;
import medlink.project.schedulingservice.dto.CreneauDayDetailsDto;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.CreneauCalendarQueryService;
import medlink.project.schedulingservice.service.PraticienIdentityService;
import medlink.project.schedulingservice.validtaion.OnCreate;
import medlink.project.schedulingservice.validtaion.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/creneaux")
@RequiredArgsConstructor
public class CreneauController {

    private final CreneauService service;
    private final PraticienIdentityService praticienIdentityService;
    private final CreneauCalendarQueryService calendarQueryService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreneauDto> create(
            @Validated(OnCreate.class) @RequestBody CreneauDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreneauDto> update(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody CreneauDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRATICIEN')")
    public ResponseEntity<CreneauDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CreneauDto>> getAll(
            @PageableDefault(size = 10, sort = "dateDebut") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenerationCreneauxResponse> generate(
            @Valid @RequestBody GenerationCreneauxRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.generate(request));
    }

    @GetMapping("/calendar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CreneauDto>> getCalendar(
            @RequestParam(required = false) Long praticienId,
            @RequestParam(required = false) Long cliniqueId,
            @RequestParam LocalDateTime debut,
            @RequestParam LocalDateTime fin
    ) {
        return ResponseEntity.ok(service.getCalendar(
                praticienId, cliniqueId, debut, fin
        ));
    }

    @GetMapping("/booking/available")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<List<BookingCreneauDto>> getAvailableForBooking(
            @RequestParam Long praticienId,
            @RequestParam Long cliniqueId,
            @RequestParam LocalDateTime debut,
            @RequestParam LocalDateTime fin
    ) {
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.ok(service.getCalendar(praticienId, cliniqueId, debut, fin)
                .stream()
                .filter(slot -> slot.getStatut() == StatutCreneau.DISPONIBLE)
                .filter(slot -> slot.getDateDebut().isAfter(now))
                .map(slot -> new BookingCreneauDto(
                        slot.getId(), slot.getPraticienId(), slot.getCliniqueId(),
                        slot.getDateDebut(), slot.getDateFin()))
                .toList());
    }

    @GetMapping("/calendar/month")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreneauMonthDto> getMonth(
            @RequestParam Long praticienId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return ResponseEntity.ok(calendarQueryService.getMonth(praticienId, month));
    }

    @GetMapping("/calendar/day")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreneauDayDetailsDto> getDay(
            @RequestParam Long praticienId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(calendarQueryService.getDay(praticienId, date));
    }

    @GetMapping("/me/calendar")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<List<CreneauDto>> getMyCalendar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam LocalDateTime debut,
            @RequestParam LocalDateTime fin
    ) {
        long praticienId = praticienIdentityService
                .requireActivePraticienId(jwt.getSubject());
        return ResponseEntity.ok(service.getCalendar(
                praticienId, null, debut, fin
        ));
    }

    @GetMapping("/get-by-praticien-id/{praticienId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CreneauDto>> getByPraticien(
            @PathVariable Long praticienId,
            @PageableDefault(size = 10, sort = "dateDebut") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByPraticien(praticienId, pageable));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
