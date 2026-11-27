package medlink.project.directoyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.CliniqueDto;
import medlink.project.directoyservice.dto.BookingCliniqueDto;
import medlink.project.directoyservice.service.CliniqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliniques")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CliniqueController {

    private final CliniqueService cliniqueService;

    @PostMapping
    public ResponseEntity<CliniqueDto> create(@Valid @RequestBody CliniqueDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cliniqueService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CliniqueDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cliniqueService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CliniqueDto>> getAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(cliniqueService.getAll(pageable));
    }

    @GetMapping("/booking/active")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<Page<BookingCliniqueDto>> getActiveForBooking(
            @PageableDefault(size = 100, sort = "nom") Pageable pageable
    ) {
        return ResponseEntity.ok(cliniqueService.getAll(pageable)
                .map(clinique -> new BookingCliniqueDto(
                        clinique.getId(), clinique.getNom(), clinique.getVille())));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CliniqueDto>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(cliniqueService.search(keyword, pageable));
    }

    @GetMapping("/by-ville")
    public ResponseEntity<Page<CliniqueDto>> getByVille(
            @RequestParam String ville,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(cliniqueService.getByVille(ville, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CliniqueDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CliniqueDto dto
    ) {
        return ResponseEntity.ok(cliniqueService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cliniqueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
