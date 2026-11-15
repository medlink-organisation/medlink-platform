package medlink.project.praticienservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.entity.Praticien;
import medlink.project.praticienservice.entity.Specialite;
import medlink.project.praticienservice.exception.BusinessException;
import medlink.project.praticienservice.exception.ResourceNotFoundException;
import medlink.project.praticienservice.mapper.PraticienMapper;
import medlink.project.praticienservice.repository.PraticienRepository;
import medlink.project.praticienservice.validator.PraticienValidator;
import medlink.project.praticienservice.validator.SpecialiteValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PraticienServiceImpl implements PraticienService {

    private final PraticienRepository repository;
    private final PraticienMapper mapper;
    private final PraticienValidator validator;
    private final SpecialiteValidator specialiteValidator;

    @Override
    @Transactional
    public PraticienDto create(PraticienDto dto) {

        validator.validateCreate(dto);

        Specialite specialite = specialiteValidator
                .validateExists(dto.getSpecialiteId());

        Praticien praticien = mapper.toEntity(dto);

        praticien.setSpecialite(specialite);
        praticien.setActif(true);

        return mapper.toDto(repository.save(praticien));
    }

    @Override
    @Transactional
    public PraticienDto update(Long id, PraticienDto dto) {

        Praticien praticien = validator.validateUpdate(id, dto);

        mapper.updateEntity(dto, praticien);

        if (dto.getSpecialiteId() != null) {

            Specialite specialite = specialiteValidator
                    .validateExists(dto.getSpecialiteId());

            praticien.setSpecialite(specialite);
        }

        return mapper.toDto(repository.save(praticien));
    }

    @Override
    public PraticienDto getById(Long id) {

        Praticien praticien = validator.validateExists(id);

        return mapper.toDto(praticien);
    }

    @Override
    public PraticienDto getByKeycloakId(String keycloakId) {

        Praticien praticien = repository
                .findByKeycloakId(keycloakId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Praticien introuvable avec KeycloakId : "
                                        + keycloakId
                        )
                );

        return mapper.toDto(praticien);
    }

    @Override
    public PraticienDto getByEmail(String email) {

        Praticien praticien = repository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Praticien introuvable avec email : "
                                        + email
                        )
                );

        return mapper.toDto(praticien);
    }

    @Override
    public Page<PraticienDto> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<PraticienDto> getBySpecialite(
            Long specialiteId,
            Pageable pageable
    ) {

        specialiteValidator.validateExists(specialiteId);

        return repository
                .findBySpecialiteId(specialiteId, pageable)
                .map(mapper::toDto);
    }
    @Override
    @Transactional
    public PraticienDto changeStatus(Long id, Boolean actif) {

        if (actif == null) {
            throw new BusinessException("Statut obligatoire");
        }

        Praticien praticien = validator.validateExists(id);

        praticien.setActif(actif);

        return mapper.toDto(repository.save(praticien));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Praticien praticien = validator.validateDelete(id);

        repository.delete(praticien);
    }
}