package medlink.project.praticienservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.praticienservice.dto.SpecialiteDto;
import medlink.project.praticienservice.dto.BookingSpecialiteDto;
import medlink.project.praticienservice.entity.Specialite;
import medlink.project.praticienservice.exception.BusinessException;
import medlink.project.praticienservice.exception.ResourceNotFoundException;
import medlink.project.praticienservice.mapper.SpecialiteMapper;
import medlink.project.praticienservice.repository.SpecialiteRepository;
import medlink.project.praticienservice.service.SpecialiteService;
import medlink.project.praticienservice.validator.SpecialiteValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialiteServiceImpl implements SpecialiteService {

    private final SpecialiteRepository repository;
    private final SpecialiteMapper mapper;
    private final SpecialiteValidator validator;

    // CREATE
    @Override
    @Transactional
    public SpecialiteDto create(SpecialiteDto dto) {

        validator.validateCreate(dto);

        Specialite specialite = mapper.toEntity(dto);

        specialite.setActif(true);

        return mapper.toDto(repository.save(specialite));
    }

    // UPDATE
    @Override
    @Transactional
    public SpecialiteDto update(Long id, SpecialiteDto dto) {

        Specialite specialite = validator.validateUpdate(id, dto);

        mapper.updateEntity(dto, specialite);

        return mapper.toDto(repository.save(specialite));
    }

    // GET BY ID
    @Override
    public SpecialiteDto getById(Long id) {

        Specialite specialite = validator.validateExists(id);

        return mapper.toDto(specialite);
    }

    // GET BY CODE
    @Override
    public SpecialiteDto getByCode(String code) {

        Specialite specialite = repository
                .findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Spécialité introuvable avec code : "
                                        + code
                        )
                );

        return mapper.toDto(specialite);
    }

    // GET ALL WITH PAGINATION
    @Override
    public Page<SpecialiteDto> getAll(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public Page<BookingSpecialiteDto> getActiveForBooking(Pageable pageable) {
        return repository.findByActifTrue(pageable)
                .map(specialite -> new BookingSpecialiteDto(
                        specialite.getId(), specialite.getCode(), specialite.getNom(),
                        specialite.getDescription()));
    }

    @Override
    @Transactional
    public SpecialiteDto changeStatus(Long id, Boolean actif) {

        if (actif == null) {
            throw new BusinessException("Statut obligatoire");
        }

        Specialite specialite = validator.validateExists(id);

        specialite.setActif(actif);

        return mapper.toDto(repository.save(specialite));
    }
    @Override
    @Transactional
    public void delete(Long id) {

        Specialite specialite = validator.validateDelete(id);

        repository.delete(specialite);
    }
}
