package medlink.project.schedulingservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.service.CreneauService;
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

@RestController
@RequestMapping("/creneaux")
@RequiredArgsConstructor
public class CreneauController {

    private final CreneauService service;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('PRATICIEN', 'ADMIN')")
    public ResponseEntity<CreneauDto> create(
            @Validated(OnCreate.class) @RequestBody CreneauDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('PRATICIEN', 'ADMIN')")
    public ResponseEntity<CreneauDto> update(
            @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody CreneauDto dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CreneauDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CreneauDto>> getAll(
            @PageableDefault(size = 10, sort = "dateDebut") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/get-by-praticien-id/{praticienId}")
    public ResponseEntity<Page<CreneauDto>> getByPraticien(
            @PathVariable Long praticienId,
            @PageableDefault(size = 10, sort = "dateDebut") Pageable pageable
    ) {
        return ResponseEntity.ok(service.getByPraticien(praticienId, pageable));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('PRATICIEN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
