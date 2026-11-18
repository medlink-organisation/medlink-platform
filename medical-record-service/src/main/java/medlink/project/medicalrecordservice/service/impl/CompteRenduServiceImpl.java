package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.CompteRenduMapper;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.service.CompteRenduService;
import medlink.project.medicalrecordservice.validator.CompteRenduValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompteRenduServiceImpl implements CompteRenduService {

    private final CompteRenduRepository repository;
    private final CompteRenduValidator validator;
    private final CompteRenduMapper mapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final MedicalRecordAccessService accessService;

    @Override
    @Transactional
    public CompteRenduDto create(CompteRenduDto dto) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanWrite(dto.getDossierMedicalId());
        DossierMedical dossier = validator.validateDossierExists(dto.getDossierMedicalId());
        validator.validateRendezVousNotAlreadyUsed(dto.getRendezVousId());
        validator.validatePraticienIsProvided(dto.getPraticienId());
        CompteRendu entity = mapper.toEntity(dto);
        entity.setDossierMedical(dossier);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CompteRenduDto update(UUID id, CompteRenduDto dto) {
        authenticatedUserService.requireAuthenticatedJwt();
        validateRequestId(id, dto.getId());
        CompteRendu entity = validator.requireExists(id);
        accessService.checkCanWrite(entity.getDossierMedical().getId());
        if (!Objects.equals(entity.getDossierMedical().getId(), dto.getDossierMedicalId())
                || !Objects.equals(entity.getRendezVousId(), dto.getRendezVousId())
                || !Objects.equals(entity.getPraticienId(), dto.getPraticienId())) {
            throw new BusinessValidationException(
                    "Le dossier, le rendez-vous et le praticien d'un compte rendu ne peuvent pas être modifiés."
            );
        }
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CompteRenduDto getById(UUID id) {
        authenticatedUserService.requireAuthenticatedJwt();
        CompteRendu entity = validator.requireExists(id);
        accessService.checkCanRead(entity.getDossierMedical().getId());
        return mapper.toDto(entity);
    }

    @Override
    public Page<CompteRenduDto> getByDossier(Long dossierId, Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanRead(dossierId);
        validator.validateDossierExists(dossierId);
        return repository.findByDossierMedicalId(dossierId, pageable).map(mapper::toDto);
    }

    @Override
    public Page<CompteRenduDto> getByPraticien(UUID praticienId, Pageable pageable) {
        authenticatedUserService.requireAuthenticatedJwt();
        accessService.checkCanListByPractitioner(praticienId);
        validator.validatePraticienIsProvided(praticienId);
        return repository.findByPraticienId(praticienId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        authenticatedUserService.requireAuthenticatedJwt();
        CompteRendu entity = validator.requireExists(id);
        accessService.checkCanWrite(entity.getDossierMedical().getId());
        throw new BusinessValidationException(
                "La suppression d'un compte rendu nécessite une politique de conservation dédiée."
        );
    }

    private void validateRequestId(UUID pathId, UUID dtoId) {
        if (!Objects.equals(pathId, dtoId)) {
            throw new BusinessValidationException(
                    "L'identifiant du DTO doit correspondre à celui de la ressource."
            );
        }
    }
}
