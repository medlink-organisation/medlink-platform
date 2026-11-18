package medlink.project.medicalrecordservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DossierMedicalValidator {

    private final DossierMedicalRepository repository;

    public void validatePatientNotAlreadyHasDossier(Long patientId) {
        if (patientId == null) {
            throw new BusinessValidationException("L'identifiant du patient est obligatoire.");
        }
        if (repository.existsByPatientId(patientId)) {
            throw new ResourceAlreadyExistsException(
                    "Le patient " + patientId + " possède déjà un dossier médical."
            );
        }
    }

    public DossierMedical requireExists(Long dossierId) {
        if (dossierId == null) {
            throw new BusinessValidationException("L'identifiant du dossier médical est obligatoire.");
        }
        return repository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dossier médical introuvable : " + dossierId
                ));
    }

    public DossierMedical requireByPatientId(Long patientId) {
        if (patientId == null) {
            throw new BusinessValidationException("L'identifiant du patient est obligatoire.");
        }
        return repository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aucun dossier médical pour le patient : " + patientId
                ));
    }
}
