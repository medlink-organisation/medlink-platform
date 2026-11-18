package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.service.DossierMedicalService;
import medlink.project.medicalrecordservice.validator.DossierMedicalValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DossierMedicalServiceImpl implements DossierMedicalService {

    private final DossierMedicalRepository repository;
    private final DossierMedicalValidator validator;
    private final DossierMedicalMapper mapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final MedicalRecordAccessService accessService;

    @Override
    @Transactional
    public DossierMedicalDto create(DossierMedicalDto dto) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanCreateForPatient(dto.getPatientId());
        validator.validatePatientNotAlreadyHasDossier(dto.getPatientId());
        DossierMedical entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public DossierMedicalDto update(Long id, DossierMedicalDto dto) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanWrite(id);
        validateRequestId(id, dto.getId());
        DossierMedical entity = validator.requireExists(id);
        if (!Objects.equals(entity.getPatientId(), dto.getPatientId())) {
            throw new BusinessValidationException(
                    "Le patient propriétaire d'un dossier médical ne peut pas être modifié."
            );
        }
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public DossierMedicalDto getById(Long id) {
        authenticatedUserService.requireAuthenticatedJwt();
        DossierMedical dossier = validator.requireExists(id);
        accessService.checkCanRead(dossier.getId());
        return mapper.toDto(dossier);
    }

    @Override
    public DossierMedicalDto getByPatientId(Long patientId) {
        authenticatedUserService.requireAuthenticatedJwt();
        DossierMedical dossier = validator.requireByPatientId(patientId);
        accessService.checkCanRead(dossier.getId());
        return mapper.toDto(dossier);
    }

    @Override
    public Page<DossierMedicalDto> getAll(Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanListAll();
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanWrite(id);
        DossierMedical entity = validator.requireExists(id);
        if (!entity.getResultatsExamens().isEmpty()
                || !entity.getComptesRendus().isEmpty()
                || !entity.getDocuments().isEmpty()) {
            throw new BusinessValidationException(
                    "Un dossier contenant des données médicales doit être archivé selon une politique dédiée."
            );
        }
        repository.delete(entity);
    }

    private void validateRequestId(Long pathId, Long dtoId) {
        if (!Objects.equals(pathId, dtoId)) {
            throw new BusinessValidationException(
                    "L'identifiant du DTO doit correspondre à celui de la ressource."
            );
        }
    }
}
