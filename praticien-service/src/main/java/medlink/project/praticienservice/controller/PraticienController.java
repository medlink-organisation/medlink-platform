package medlink.project.praticienservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.dto.BookingPraticienDto;
import medlink.project.praticienservice.service.PraticienService;
import medlink.project.praticienservice.validtaion.OnCreate;
import medlink.project.praticienservice.validtaion.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/praticiens")
@RequiredArgsConstructor
public class PraticienController {

    private final PraticienService service;

    @PreAuthorize("hasRole('ADMIN')")

    @PostMapping("/create")
    public ResponseEntity<PraticienDto> create(
            @Validated(OnCreate.class)
            @RequestBody PraticienDto dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<PraticienDto> update(
            @PathVariable Long id,
            @Validated(OnUpdate.class)
            @RequestBody PraticienDto dto
    ) {

        return ResponseEntity.ok(service.update(id, dto));
    }
    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/get/{id}")
    public ResponseEntity<PraticienDto> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(service.getById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")


    @GetMapping("/get-by-idKycloak/{keycloakId}")
    public ResponseEntity<PraticienDto> getByKeycloakId(
            @PathVariable String keycloakId
    ) {

        return ResponseEntity.ok(
                service.getByKeycloakId(keycloakId)
        );
    }
    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/get-by-email/email")
    public ResponseEntity<PraticienDto> getByEmail(
            @RequestParam String email
    ) {

        return ResponseEntity.ok(service.getByEmail(email));
    }
    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/getAll")
    public ResponseEntity<Page<PraticienDto>> getAll(
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {

        return ResponseEntity.ok(service.getAll(pageable));
    }
    @PreAuthorize("hasRole('ADMIN')")


    @GetMapping("/get-by-specialite/{specialiteId}")
    public ResponseEntity<Page<PraticienDto>> getBySpecialite(
            @PathVariable Long specialiteId,
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getBySpecialite(specialiteId, pageable)
        );
    }

    @GetMapping("/booking/active/by-specialite/{specialiteId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Page<BookingPraticienDto>> getActiveForBooking(
            @PathVariable Long specialiteId,
            @PageableDefault(size = 100, sort = "nom") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getActiveForBooking(specialiteId, pageable));
    }
    @PreAuthorize("hasRole('ADMIN')")

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<PraticienDto> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean actif
    ) {
        return ResponseEntity.ok(
                service.changeStatus(id, actif)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
