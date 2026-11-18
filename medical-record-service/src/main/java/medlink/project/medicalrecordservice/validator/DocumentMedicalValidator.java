package medlink.project.medicalrecordservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentMedicalValidator {

    private final DocumentMedicalRepository documentMedicalRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public DocumentMedical requireExists(Long documentId) {
        if (documentId == null) {
            throw new BusinessValidationException("L'identifiant du document est obligatoire.");
        }
        return documentMedicalRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Document médical introuvable : " + documentId
                ));
    }

    public void validateDossierExists(Long dossierId) {
        if (dossierId == null) {
            throw new BusinessValidationException("L'identifiant du dossier médical est obligatoire.");
        }
        dossierMedicalRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dossier médical introuvable : " + dossierId
                ));
    }

    public void validateObjectKeyUnique(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) {
            throw new BusinessValidationException("La clé de stockage MinIO est obligatoire.");
        }
        if (documentMedicalRepository.existsByObjectKey(objectKey)) {
            throw new ResourceAlreadyExistsException(
                    "Un document utilise déjà la clé objet : " + objectKey
            );
        }
    }

    public void validateMetadata(
            String filename,
            String contentType,
            String objectKey,
            String checksum,
            Long size
    ) {
        if (filename == null || filename.isBlank()) {
            throw new BusinessValidationException("Le nom du fichier est obligatoire.");
        }
        if (!"application/pdf".equals(contentType)) {
            throw new BusinessValidationException("Le document doit être un PDF.");
        }
        if (checksum == null || !checksum.matches("[0-9a-f]{64}")) {
            throw new BusinessValidationException("Le checksum SHA-256 est invalide.");
        }
        if (size == null || size <= 0) {
            throw new BusinessValidationException("La taille du document est invalide.");
        }
        validateObjectKeyUnique(objectKey);
    }

    public void validateDocumentBelongsToDossier(DocumentMedical document, Long dossierId) {
        if (document == null || document.getDossierMedical() == null
                || !document.getDossierMedical().getId().equals(dossierId)) {
            throw new BusinessValidationException(
                    "Le document appartient à un autre dossier médical."
            );
        }
    }
}
