package medlink.project.workflowservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.dto.PatientBookingRequest;
import medlink.project.workflowservice.service.PatientBookingService;
import medlink.project.workflowservice.service.PatientIdentityService;
import medlink.project.workflowservice.service.WorkflowService.WorkflowStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient/appointments")
@RequiredArgsConstructor
public class PatientBookingController {

    private final PatientIdentityService patientIdentityService;
    private final PatientBookingService patientBookingService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<WorkflowStatus> book(
            @Valid @RequestBody PatientBookingRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        long patientId = patientIdentityService.requireActivePatientId(jwt);
        WorkflowStatus status = patientBookingService.book(
                patientId,
                request.slotId(),
                request.reason(),
                jwt.getSubject(),
                request.idempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @PatchMapping("/{rendezVousId}/annuler")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<WorkflowStatus> cancel(
            @PathVariable String rendezVousId,
            @RequestParam(defaultValue = "Annulation demandée par le patient") String reason,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long patientId = patientIdentityService.requireActivePatientId(jwt);
        return ResponseEntity.ok(patientBookingService.cancel(
                patientId,
                rendezVousId,
                reason
        ));
    }

    @PatchMapping("/{rendezVousId}/reporter/{newSlotId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<WorkflowStatus> reschedule(
            @PathVariable String rendezVousId,
            @PathVariable long newSlotId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        long patientId = patientIdentityService.requireActivePatientId(jwt);
        return ResponseEntity.ok(patientBookingService.reschedule(
                patientId,
                rendezVousId,
                newSlotId
        ));
    }
}
