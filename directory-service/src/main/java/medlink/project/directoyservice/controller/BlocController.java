package medlink.project.directoyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.BlocDto;
import medlink.project.directoyservice.service.BlocService;
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
@RequestMapping("/blocs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BlocController {

    private final BlocService blocService;

    @PostMapping
    public ResponseEntity<BlocDto> create(@Valid @RequestBody BlocDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blocService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlocDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blocService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BlocDto>> getAll(
            @PageableDefault(size = 20, sort = "idBloc") Pageable pageable
    ) {
        return ResponseEntity.ok(blocService.getAll(pageable));
    }

    @GetMapping("/by-clinique/{cliniqueId}")
    public ResponseEntity<Page<BlocDto>> getByClinique(
            @PathVariable Long cliniqueId,
            @PageableDefault(size = 20, sort = "idBloc") Pageable pageable
    ) {
        return ResponseEntity.ok(blocService.getByClinique(cliniqueId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlocDto>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "idBloc") Pageable pageable
    ) {
        return ResponseEntity.ok(blocService.search(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlocDto> update(@PathVariable Long id, @Valid @RequestBody BlocDto dto) {
        return ResponseEntity.ok(blocService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blocService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
