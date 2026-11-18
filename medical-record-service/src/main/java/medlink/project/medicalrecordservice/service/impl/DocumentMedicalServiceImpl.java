package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.dto.DocumentMedicalDto;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.DocumentMedicalMapper;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.service.DocumentMedicalService;
import medlink.project.medicalrecordservice.validator.DocumentMedicalValidator;
import medlink.project.medicalrecordservice.validator.ResultatExamenValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentMedicalServiceImpl implements DocumentMedicalService {

    private final DocumentMedicalRepository repository;
    private final DocumentMedicalValidator documentValidator;
    private final ResultatExamenValidator resultatExamenValidator;
    private final DocumentMedicalMapper mapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final MedicalRecordAccessService accessService;

    @Override
    public DocumentMedicalDto getById(Long id) {
        authenticatedUserService.requireAuthenticatedJwt();
        DocumentMedical document = documentValidator.requireExists(id);
        accessService.checkCanRead(document.getDossierMedical().getId());
        return mapper.toDto(document);
    }

    @Override
    public Page<DocumentMedicalDto> getByDossier(Long dossierId, Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanRead(dossierId);
        documentValidator.validateDossierExists(dossierId);
        return repository.findByDossierMedicalId(dossierId, pageable).map(mapper::toDto);
    }

    @Override
    public Page<DocumentMedicalDto> getByExamen(UUID examenId, Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        ResultatExamen resultat = resultatExamenValidator.requireExists(examenId);
        accessService.checkCanRead(resultat.getDossierMedical().getId());
        if (resultat.getDocument() == null) {
            throw new BusinessValidationException("Aucun document n'est associé au résultat d'examen.");
        }
        List<DocumentMedicalDto> content = pageable.isPaged() && pageable.getOffset() > 0
                ? List.of()
                : List.of(mapper.toDto(resultat.getDocument()));
        return new PageImpl<>(
                content,
                pageable,
                1
        );
    }
}
