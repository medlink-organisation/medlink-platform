package medlink.project.medicalrecordservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResultatExamenValidator {

    private final ResultatExamenRepository resultatExamenRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public ResultatExamen requireExists(UUID examenId) {
        if (examenId == null) {
            throw new BusinessValidationException("L'identifiant du résultat d'examen est obligatoire.");
        }
        return resultatExamenRepository.findById(examenId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Résultat d'examen introuvable : " + examenId
                ));
    }

    public DossierMedical validateDossierExists(Long dossierId) {
        if (dossierId == null) {
            throw new BusinessValidationException("L'identifiant du dossier médical est obligatoire.");
        }
        return dossierMedicalRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dossier médical introuvable : " + dossierId
                ));
    }

    public void validateExamenBelongsToDossier(UUID examenId, Long dossierId) {
        ResultatExamen examen = requireExists(examenId);
        if (!examen.getDossierMedical().getId().equals(dossierId)) {
            throw new BusinessValidationException(
                    "Le résultat d'examen n'appartient pas au dossier indiqué."
            );
        }
    }

    public void validateUploadMetadata(String typeExamen, LocalDate dateExamen) {
        if (typeExamen == null || typeExamen.isBlank()) {
            throw new BusinessValidationException("Le type d'examen est obligatoire.");
        }
        if (typeExamen.length() > 150) {
            throw new BusinessValidationException(
                    "Le type d'examen ne doit pas dépasser 150 caractères."
            );
        }
        if (dateExamen == null) {
            throw new BusinessValidationException("La date de l'examen est obligatoire.");
        }
        if (dateExamen.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("La date de l'examen ne peut pas être future.");
        }
    }
}
