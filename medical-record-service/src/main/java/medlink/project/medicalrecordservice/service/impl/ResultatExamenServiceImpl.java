package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.OrphanedObjectCleanupException;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import medlink.project.medicalrecordservice.service.ClamAvService;
import medlink.project.medicalrecordservice.service.DownloadedDocument;
import medlink.project.medicalrecordservice.service.ResultatExamenService;
import medlink.project.medicalrecordservice.service.Sha256ChecksumService;
import medlink.project.medicalrecordservice.storage.ObjectStorageService;
import medlink.project.medicalrecordservice.validator.FileSecurityValidator;
import medlink.project.medicalrecordservice.validator.ResultatExamenValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultatExamenServiceImpl implements ResultatExamenService {

    private final ResultatExamenRepository repository;
    private final ResultatExamenValidator validator;
    private final ResultatExamenMapper mapper;
    private final FileSecurityValidator fileSecurityValidator;
    private final ClamAvService clamAvService;
    private final Sha256ChecksumService checksumService;
    private final ObjectStorageService objectStorageService;
    private final ResultatExamenPersistenceService persistenceService;
    private final AuthenticatedUserService authenticatedUserService;
    private final MedicalRecordAccessService accessService;

    @Override
    public ResultatExamenDto uploadResultat(
            Long dossierId,
            UUID rendezVousId,
            String typeExamen,
            LocalDate dateExamen,
            String conclusion,
            MultipartFile file
    ) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanAttachResult(dossierId, rendezVousId);
        DossierMedical dossier = validator.validateDossierExists(dossierId);
        validator.validateUploadMetadata(typeExamen, dateExamen);
        byte[] pdfBytes = fileSecurityValidator.validateAndReadPdf(file);

        clamAvService.scan(new ByteArrayInputStream(pdfBytes));
        String checksum = checksumService.calculate(new ByteArrayInputStream(pdfBytes));

        String objectKey = objectStorageService.upload(
                new ByteArrayInputStream(pdfBytes),
                pdfBytes.length,
                "application/pdf"
        );

        try {
            return persistenceService.persistUploadedResult(
                    dossier,
                    typeExamen.trim(),
                    dateExamen,
                    normalizeConclusion(conclusion),
                    sanitizeFilename(file.getOriginalFilename()),
                    pdfBytes.length,
                    checksum,
                    objectKey
            );
        } catch (RuntimeException persistenceFailure) {
            compensateStorage(objectKey, persistenceFailure);
            throw persistenceFailure;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResultatExamenDto getById(UUID id) {
        authenticatedUserService.requireAuthenticatedJwt();
        ResultatExamen result = validator.requireExists(id);
        accessService.checkCanRead(result.getDossierMedical().getId());
        return mapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResultatExamenDto> getByDossier(Long dossierId, Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanRead(dossierId);
        validator.validateDossierExists(dossierId);
        return repository.findByDossierMedicalId(dossierId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DownloadedDocument download(UUID id) {
        authenticatedUserService.requireAuthenticatedJwt();
        ResultatExamen result = validator.requireExists(id);
        accessService.checkCanRead(result.getDossierMedical().getId());
        if (result.getDocument() == null) {
            throw new BusinessValidationException("Aucun PDF n'est associé à ce résultat d'examen.");
        }
        return new DownloadedDocument(
                objectStorageService.download(result.getDocument().getObjectKey()),
                result.getDocument().getNomFichier(),
                result.getDocument().getTypeMime(),
                result.getDocument().getTailleOctets()
        );
    }

    @Override
    public void delete(UUID id) {
        authenticatedUserService.requireAuthenticatedJwt();
        ResultatExamen result = validator.requireExists(id);
        accessService.checkCanWrite(result.getDossierMedical().getId());
        throw new BusinessValidationException(
                "La suppression d'un résultat médical nécessite une politique de conservation dédiée."
        );
    }

    private void compensateStorage(String objectKey, RuntimeException persistenceFailure) {
        try {
            objectStorageService.delete(objectKey);
        } catch (RuntimeException cleanupFailure) {
            cleanupFailure.addSuppressed(persistenceFailure);
            throw new OrphanedObjectCleanupException(
                    "La persistance a échoué et l'objet MinIO orphelin n'a pas pu être nettoyé.",
                    cleanupFailure
            );
        }
    }

    private String sanitizeFilename(String originalFilename) {
        String value = originalFilename == null ? "resultat-examen.pdf" : originalFilename;
        value = value.replace('\\', '/');
        value = value.substring(value.lastIndexOf('/') + 1)
                .replaceAll("[\\p{Cntrl}\\r\\n]", "_")
                .trim();
        if (value.isBlank() || !value.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            return "resultat-examen.pdf";
        }
        return value.length() <= 255 ? value : "resultat-examen.pdf";
    }

    private String normalizeConclusion(String conclusion) {
        return conclusion == null || conclusion.isBlank() ? null : conclusion.trim();
    }
}
