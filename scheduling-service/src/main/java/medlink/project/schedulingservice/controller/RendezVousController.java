package medlink.project.schedulingservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.dto.RendezVousAgendaDto;
import medlink.project.schedulingservice.dto.PraticienPatientDto;
import medlink.project.schedulingservice.dto.PatientRendezVousDto;
import medlink.project.schedulingservice.service.PatientIdentityService;
import medlink.project.schedulingservice.service.PraticienIdentityService;
import medlink.project.schedulingservice.service.RendezVousService;
import medlink.project.schedulingservice.validtaion.OnCreate;
import medlink.project.schedulingservice.validtaion.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService service;
    private final PraticienIdentityService praticienIdentityService;
    private final PatientIdentityService patientIdentityService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<Page<PatientRendezVousDto>> getMyAppointments(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        long patientId = patientIdentityService
                .requireActivePatientId(jwt.getSubject());
        return ResponseEntity.ok(service.getForPatient(patientId, pageable));
    }

    @GetMapping("/me/agenda")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<List<RendezVousAgendaDto>> getMyAgenda(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam LocalDateTime debut,
            @RequestParam LocalDateTime fin
    ) {
        long praticienId = praticienIdentityService
                .requireActivePraticienId(jwt.getSubject());
        return ResponseEntity.ok(service.getAgendaByPraticien(
                praticienId, debut, fin
        ));
    }

    @GetMapping("/me/due")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<List<RendezVousAgendaDto>> getMyDueAppointments(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "15") int minutesBefore,
            @RequestParam(defaultValue = "60") int minutesAfter
    ) {
        long praticienId = praticienIdentityService
                .requireActivePraticienId(jwt.getSubject());
        return ResponseEntity.ok(service.getDueByPraticien(
                praticienId, minutesBefore, minutesAfter
        ));
    }

    @GetMapping("/me/patients")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<Page<PraticienPatientDto>> getMyPatients(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        long praticienId = praticienIdentityService
                .requireActivePraticienId(jwt.getSubject());

        return ResponseEntity.ok(
                service.getPatientsByPraticien(
                        praticienId,
                        pageable
                )
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RendezVousDto> create(
            @Validated(OnCreate.class) @RequestBody RendezVousDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RendezVousDto> update(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody RendezVousDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/get-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RendezVousDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/getall")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RendezVousDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/get-by-patientid/{patientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RendezVousDto>> getByPatient(
            @PathVariable Long patientId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByPatient(patientId, pageable));
    }

    @GetMapping("/get-by-creneau/{creneauId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RendezVousDto>> getByCreneau(
            @PathVariable Long creneauId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByCreneau(creneauId, pageable));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
