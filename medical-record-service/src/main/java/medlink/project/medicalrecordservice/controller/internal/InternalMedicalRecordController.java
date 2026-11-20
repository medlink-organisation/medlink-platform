package medlink.project.medicalrecordservice.controller.internal;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.service.InternalMedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/medical-records")
@RequiredArgsConstructor
@PreAuthorize("hasRole('WORKFLOW_SERVICE')")
public class InternalMedicalRecordController {

    private final InternalMedicalRecordService service;

    @PutMapping("/patients/{patientId}")
    public DossierMedicalDto ensureDossier(@PathVariable Long patientId) {
        return service.ensureDossierForPatient(patientId);
    }

    @GetMapping("/patients/{patientId}")
    public DossierMedicalDto getDossier(@PathVariable Long patientId) {
        return service.getDossierByPatientId(patientId);
    }

    @PutMapping("/{dossierId}/reports/{rendezVousId}")
    public ResponseEntity<CompteRenduDto> saveCompteRendu(
            @PathVariable Long dossierId,
            @PathVariable UUID rendezVousId,
            @RequestBody CompteRenduDto dto
    ) {
        return ResponseEntity.ok(service.saveCompteRendu(
                dossierId,
                rendezVousId,
                dto.getPraticienId(),
                dto.getContenu()
        ));
    }
}
