package medlink.project.schedulingservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.service.RendezVousService;
import medlink.project.schedulingservice.validtaion.OnCreate;
import medlink.project.schedulingservice.validtaion.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService service;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<RendezVousDto> create(
            @Validated(OnCreate.class) @RequestBody RendezVousDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<RendezVousDto> update(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody RendezVousDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<RendezVousDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/getall")
    public ResponseEntity<Page<RendezVousDto>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/get-by-patientid/{patientId}")
    public ResponseEntity<Page<RendezVousDto>> getByPatient(
            @PathVariable Long patientId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByPatient(patientId, pageable));
    }

    @GetMapping("/get-by-creneau/{creneauId}")
    public ResponseEntity<Page<RendezVousDto>> getByCreneau(
            @PathVariable Long creneauId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByCreneau(creneauId, pageable));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
