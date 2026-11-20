package medlink.project.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.mapper.CreneauMapper;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.validator.CreneauValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreneauServiceImpl implements CreneauService {

    private final CreneauRepository repository;
    private final CreneauMapper mapper;
    private final CreneauValidator validator;

    @Override
    @Transactional
    public CreneauDto create(CreneauDto dto) {
        validator.validateCreate(dto);

        Creneau creneau = mapper.toEntity(dto);
        creneau.setStatut(StatutCreneau.DISPONIBLE);
        return mapper.toDto(repository.save(creneau));
    }

    @Override
    @Transactional
    public CreneauDto update(Long id, CreneauDto dto) {
        Creneau creneau = validator.validateUpdate(id, dto);
        mapper.update(dto, creneau);
        return mapper.toDto(repository.save(creneau));
    }

    @Override
    public CreneauDto getById(Long id) {
        return mapper.toDto(validator.validateExists(id));
    }

    @Override
    public Page<CreneauDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<CreneauDto> getByPraticien(Long praticienId, Pageable pageable) {
        return repository.findAllByPraticienId(praticienId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(validator.validateDelete(id));
    }

    @Override
    public boolean isDisponible(Long id) {
        return validator.isDisponible(id);
    }

    @Override
    @Transactional
    public CreneauDto reserver(Long id) {
        Creneau creneau = validator.validateDisponibleForUpdate(id);
        creneau.setStatut(StatutCreneau.RESERVE);
        return mapper.toDto(repository.save(creneau));
    }

    @Override
    @Transactional
    public CreneauDto liberer(Long id) {
        Creneau creneau = validator.validateExists(id);
        creneau.setStatut(StatutCreneau.DISPONIBLE);
        return mapper.toDto(repository.save(creneau));
    }
}
