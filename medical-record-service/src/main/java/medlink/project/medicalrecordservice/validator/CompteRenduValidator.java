package medlink.project.medicalrecordservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompteRenduValidator {

    private final CompteRenduRepository compteRenduRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public CompteRendu requireExists(UUID compteRenduId) {
        if (compteRenduId == null) {
            throw new BusinessValidationException("L'identifiant du compte rendu est obligatoire.");
        }
        return compteRenduRepository.findById(compteRenduId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte rendu introuvable : " + compteRenduId
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

    public void validateRendezVousNotAlreadyUsed(UUID rendezVousId) {
        if (rendezVousId == null) {
            throw new BusinessValidationException("L'identifiant du rendez-vous est obligatoire.");
        }
        if (compteRenduRepository.existsByRendezVousId(rendezVousId)) {
            throw new ResourceAlreadyExistsException(
                    "Un compte rendu existe déjà pour le rendez-vous : " + rendezVousId
            );
        }
    }

    public void validatePraticienIsProvided(UUID praticienId) {
        if (praticienId == null) {
            throw new BusinessValidationException("L'identifiant du praticien est obligatoire.");
        }
    }
}
