package medlink.project.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.authservice.dto.PatientPageResponse;
import medlink.project.authservice.dto.PatientResponse;
import medlink.project.authservice.dto.UpdatePatientContactRequest;
import medlink.project.authservice.service.KeycloakUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/patients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PatientController {

    private final KeycloakUserService keycloakUserService;

    @GetMapping("/{keycloakId}")
    public PatientResponse getById(@PathVariable String keycloakId) {
        return keycloakUserService.getPatientById(keycloakId);
    }

    @GetMapping
    public PatientPageResponse getAll(
            @RequestParam(defaultValue = "0") int first,
            @RequestParam(defaultValue = "20") int max
    ) {
        return keycloakUserService.getAllPatients(first, max);
    }

    @GetMapping("/by-email")
    public PatientResponse getByEmail(@RequestParam String email) {
        return keycloakUserService.getPatientByEmail(email);
    }

    @GetMapping("/{keycloakId}/exists")
    public boolean existsById(@PathVariable String keycloakId) {
        return keycloakUserService.existsPatientById(keycloakId);
    }

    @GetMapping("/exists-by-email")
    public boolean existsByEmail(@RequestParam String email) {
        return keycloakUserService.existsPatientByEmail(email);
    }

    @GetMapping("/{keycloakId}/active")
    public boolean isActive(@PathVariable String keycloakId) {
        return keycloakUserService.isPatientActive(keycloakId);
    }

    @PatchMapping("/{keycloakId}/enable")
    public ResponseEntity<Void> enable(@PathVariable String keycloakId) {
        keycloakUserService.enablePatient(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{keycloakId}/disable")
    public ResponseEntity<Void> disable(@PathVariable String keycloakId) {
        keycloakUserService.disablePatient(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{keycloakId}/roles")
    public List<String> getRoles(@PathVariable String keycloakId) {
        return keycloakUserService.getPatientRoles(keycloakId);
    }

    @PatchMapping("/{keycloakId}/contact")
    public PatientResponse updateContact(
            @PathVariable String keycloakId,
            @Valid @RequestBody UpdatePatientContactRequest request
    ) {
        return keycloakUserService.updatePatientContact(keycloakId, request);
    }
}
