package medlink.project.directoyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.ServiceMedicaleDto;
import medlink.project.directoyservice.service.ServiceMedicaleService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services-medicales")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ServiceMedicaleController {

    private final ServiceMedicaleService serviceMedicaleService;

    @PostMapping
    public ResponseEntity<ServiceMedicaleDto> create(@Valid @RequestBody ServiceMedicaleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceMedicaleService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceMedicaleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMedicaleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ServiceMedicaleDto>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.getAll(pageable));
    }

    @GetMapping("/by-clinique/{cliniqueId}")
    public ResponseEntity<Page<ServiceMedicaleDto>> getByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.getByClinique(cliniqueId, pageable));
    }

    @GetMapping("/by-bloc/{blocId}")
    public ResponseEntity<Page<ServiceMedicaleDto>> getByBloc(
            @PathVariable Long blocId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.getByBloc(blocId, pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<ServiceMedicaleDto>> getActive(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.getActive(pageable));
    }

    @GetMapping("/active/by-clinique/{cliniqueId}")
    public ResponseEntity<Page<ServiceMedicaleDto>> getActiveByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.getActiveByClinique(cliniqueId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ServiceMedicaleDto>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(serviceMedicaleService.search(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceMedicaleDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceMedicaleDto dto
    ) {
        return ResponseEntity.ok(serviceMedicaleService.update(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ServiceMedicaleDto> activate(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMedicaleService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ServiceMedicaleDto> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMedicaleService.deactivate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceMedicaleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
