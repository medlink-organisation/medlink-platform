package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;

import java.util.UUID;

public interface InternalMedicalRecordService {

    DossierMedicalDto ensureDossierForPatient(Long patientId);

    DossierMedicalDto getDossierByPatientId(Long patientId);

    CompteRenduDto saveCompteRendu(
            Long dossierId,
            UUID rendezVousId,
            UUID praticienId,
            String contenu
    );
}
