package medlink.project.schedulingservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.service.CreneauService;
import medlink.project.schedulingservice.service.RendezVousService;
import medlink.project.schedulingservice.validtaion.OnCreate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole( 'ADMIN')")
public class InternalController {

    private final CreneauService creneauService;
    private final RendezVousService rendezVousService;

    @GetMapping("/creneaux/{id}/disponible")
    public ResponseEntity<Boolean> isCreneauDisponible(@PathVariable Long id) {
        return ResponseEntity.ok(creneauService.isDisponible(id));
    }

    @PostMapping("/rendez-vous/reserver")
    public ResponseEntity<RendezVousDto> reserver(
            @Validated(OnCreate.class) @RequestBody RendezVousDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rendezVousService.reserver(dto));
    }

    @PatchMapping("/rendez-vous/{id}/confirmer")
    public ResponseEntity<RendezVousDto> confirmer(@PathVariable UUID id) {
        return ResponseEntity.ok(rendezVousService.confirmer(id));
    }

    @PatchMapping("/rendez-vous/{id}/annuler")
    public ResponseEntity<RendezVousDto> annuler(@PathVariable UUID id) {
        return ResponseEntity.ok(rendezVousService.annuler(id));
    }

    @PatchMapping("/rendez-vous/{id}/reporter/{nouveauCreneauId}")
    public ResponseEntity<RendezVousDto> reporter(
            @PathVariable UUID id,
            @PathVariable Long nouveauCreneauId
    ) {
        return ResponseEntity.ok(rendezVousService.reporter(id, nouveauCreneauId));
    }

    @PatchMapping("/rendez-vous/{id}/absent")
    public ResponseEntity<RendezVousDto> marquerAbsent(@PathVariable UUID id) {
        return ResponseEntity.ok(rendezVousService.marquerAbsent(id));
    }

    @PatchMapping("/rendez-vous/{id}/terminer")
    public ResponseEntity<RendezVousDto> terminer(@PathVariable UUID id) {
        return ResponseEntity.ok(rendezVousService.terminer(id));
    }
}
