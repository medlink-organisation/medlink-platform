package medlink.project.praticienservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.praticienservice.dto.SpecialiteDto;
import medlink.project.praticienservice.dto.BookingSpecialiteDto;
import medlink.project.praticienservice.service.SpecialiteService;
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
@RequestMapping("/specialites")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class SpecialiteController {

    private final SpecialiteService service;


    @PostMapping("create")
    public ResponseEntity<SpecialiteDto> create(
            @Validated(OnCreate.class)
            @RequestBody SpecialiteDto dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    // UPDATE
    @PatchMapping("update/{id}")
    public ResponseEntity<SpecialiteDto> update(
            @PathVariable Long id,
            @Validated(OnUpdate.class)
            @RequestBody SpecialiteDto dto
    ) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<SpecialiteDto> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<SpecialiteDto> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(service.getByCode(code));
    }

    @GetMapping("/getall")
    public ResponseEntity<Page<SpecialiteDto>> getAll(
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {

        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/booking/active")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Page<BookingSpecialiteDto>> getActiveForBooking(
            @PageableDefault(size = 100, sort = "nom") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getActiveForBooking(pageable));
    }

    @GetMapping("/booking/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<BookingSpecialiteDto> getForBooking(@PathVariable Long id) {
        SpecialiteDto specialite = service.getById(id);
        return ResponseEntity.ok(new BookingSpecialiteDto(
                specialite.getId(), specialite.getCode(), specialite.getNom(), specialite.getDescription()));
    }

    @PatchMapping("/change-status/{id}")
    public ResponseEntity<SpecialiteDto> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean actif
    ) {
        return ResponseEntity.ok(
                service.changeStatus(id, actif)
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
