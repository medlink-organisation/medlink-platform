package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;

import java.util.UUID;
import java.time.LocalDateTime;

public interface InternalMedicalRecordService {

    DossierMedicalDto ensureDossierForPatient(Long patientId);

    DossierMedicalDto getDossierByPatientId(Long patientId);

    CompteRenduDto saveCompteRendu(ConsultationReportCommand command);

    CompteRenduDto saveCompteRendu(
            Long dossierId,
            UUID rendezVousId,
            UUID praticienId,
            String contenu
    );

    record ConsultationReportCommand(
            Long dossierId,
            UUID rendezVousId,
            Long praticienId,
            Long cliniqueId,
            LocalDateTime dateConsultation,
            String motif,
            String symptomes,
            String diagnostic,
            String observations,
            String traitementRecommande,
            String examensComplementaires,
            String recommandations,
            boolean controleRequis
    ) {}
}
