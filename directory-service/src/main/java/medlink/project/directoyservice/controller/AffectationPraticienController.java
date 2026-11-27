package medlink.project.directoyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.AffectationPraticienDto;
import medlink.project.directoyservice.dto.BookingAffectationDto;
import medlink.project.directoyservice.service.AffectationPraticienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/affectations-praticiens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AffectationPraticienController {

    private final AffectationPraticienService affectationService;

    @PostMapping
    public ResponseEntity<AffectationPraticienDto> create(
            @Valid @RequestBody AffectationPraticienDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(affectationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AffectationPraticienDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(affectationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AffectationPraticienDto>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService.getAll(pageable));
    }

    @GetMapping("/by-praticien/{praticienId}")
    public ResponseEntity<Page<AffectationPraticienDto>> getByPraticien(
            @PathVariable Long praticienId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService.getByPraticien(praticienId, pageable));
    }

    @GetMapping("/active/by-praticien/{praticienId}")
    public ResponseEntity<Page<AffectationPraticienDto>> getActiveByPraticien(
            @PathVariable Long praticienId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService.getActiveByPraticien(praticienId, pageable));
    }

    @GetMapping("/booking/active/by-praticien/{praticienId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Page<BookingAffectationDto>> getBookingByPraticien(
            @PathVariable Long praticienId,
            @PageableDefault(size = 100, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService
                .getActiveByPraticien(praticienId, pageable)
                .map(this::toBookingDto));
    }

    @GetMapping("/by-clinique/{cliniqueId}")
    public ResponseEntity<Page<AffectationPraticienDto>> getByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService.getByClinique(cliniqueId, pageable));
    }

    @GetMapping("/active/by-clinique/{cliniqueId}")
    public ResponseEntity<Page<AffectationPraticienDto>> getActiveByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService.getActiveByClinique(cliniqueId, pageable));
    }

    @GetMapping("/booking/active/by-clinique/{cliniqueId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Page<BookingAffectationDto>> getBookingByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 100, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(affectationService
                .getActiveByClinique(cliniqueId, pageable)
                .map(this::toBookingDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AffectationPraticienDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AffectationPraticienDto dto
    ) {
        return ResponseEntity.ok(affectationService.update(id, dto));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AffectationPraticienDto> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(affectationService.desaffecter(id));
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<AffectationPraticienDto> reactivate(@PathVariable Long id) {
        return ResponseEntity.ok(affectationService.reactiver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        affectationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BookingAffectationDto toBookingDto(AffectationPraticienDto dto) {
        return new BookingAffectationDto(
                dto.getId(), dto.getPraticienId(), dto.getCliniqueId(),
                dto.getCliniqueNom(), dto.getActif(), dto.getDeleted());
    }
}
