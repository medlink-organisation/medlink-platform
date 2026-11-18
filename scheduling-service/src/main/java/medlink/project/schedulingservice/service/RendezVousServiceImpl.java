package medlink.project.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.RendezVous;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.mapper.RendezVousMapper;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import medlink.project.schedulingservice.validator.RendezVousValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RendezVousServiceImpl implements RendezVousService {

    private final RendezVousRepository repository;
    private final CreneauRepository creneauRepository;
    private final RendezVousMapper mapper;
    private final RendezVousValidator validator;

    @Override
    @Transactional
    public RendezVousDto create(RendezVousDto dto) {
        return createAndReserve(dto, StatusRendezVous.EN_ATTENTE);
    }

    @Override
    @Transactional
    public RendezVousDto reserver(RendezVousDto dto) {
        return createAndReserve(dto, StatusRendezVous.CONFIRME);
    }

    @Override
    @Transactional
    public RendezVousDto update(UUID id, RendezVousDto dto) {
        RendezVous rendezVous = validator.validateUpdate(id);
        mapper.update(dto, rendezVous);
        return mapper.toDto(repository.save(rendezVous));
    }

    @Override
    public RendezVousDto getById(UUID id) {
        return mapper.toDto(validator.validateExists(id));
    }

    @Override
    public Page<RendezVousDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<RendezVousDto> getByPatient(Long patientId, Pageable pageable) {
        return repository.findAllByPatientId(patientId, pageable).map(mapper::toDto);
    }

    @Override
    public Page<RendezVousDto> getByCreneau(Long creneauId, Pageable pageable) {
        return repository.findAllByCreneauId(creneauId, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        RendezVous rendezVous = validator.validateDelete(id);
        libererSiActif(rendezVous);
        repository.delete(rendezVous);
    }

    @Override
    @Transactional
    public RendezVousDto confirmer(UUID id) {
        RendezVous rendezVous = validator.validateConfirmation(id);
        rendezVous.setStatus(StatusRendezVous.CONFIRME);
        return mapper.toDto(repository.save(rendezVous));
    }

    @Override
    @Transactional
    public RendezVousDto annuler(UUID id) {
        RendezVous rendezVous = validator.validateActive(id);
        rendezVous.setStatus(StatusRendezVous.ANNULE);
        rendezVous.getCreneau().setStatut(StatutCreneau.DISPONIBLE);
        creneauRepository.save(rendezVous.getCreneau());
        return mapper.toDto(repository.save(rendezVous));
    }

    @Override
    @Transactional
    public RendezVousDto reporter(UUID id, Long nouveauCreneauId) {
        RendezVous rendezVous = validator.validateActive(id);

        Creneau ancienCreneau = rendezVous.getCreneau();
        if (ancienCreneau.getId().equals(nouveauCreneauId)) {
            return mapper.toDto(rendezVous);
        }

        Creneau nouveauCreneau = validator.validateReport(rendezVous, nouveauCreneauId);
        ancienCreneau.setStatut(StatutCreneau.DISPONIBLE);
        nouveauCreneau.setStatut(StatutCreneau.RESERVE);
        rendezVous.setCreneau(nouveauCreneau);

        creneauRepository.save(ancienCreneau);
        creneauRepository.save(nouveauCreneau);
        return mapper.toDto(repository.save(rendezVous));
    }

    @Override
    @Transactional
    public RendezVousDto marquerAbsent(UUID id) {
        return close(id, StatusRendezVous.ABSENT);
    }

    @Override
    @Transactional
    public RendezVousDto terminer(UUID id) {
        return close(id, StatusRendezVous.TERMINE);
    }

    private RendezVousDto createAndReserve(RendezVousDto dto, StatusRendezVous status) {
        Creneau creneau = validator.validateCreate(dto.getPatientId(), dto.getCreneauId());

        RendezVous rendezVous = mapper.toEntity(dto);
        rendezVous.setCreneau(creneau);
        rendezVous.setStatus(status);
        creneau.setStatut(StatutCreneau.RESERVE);
        creneauRepository.save(creneau);
        return mapper.toDto(repository.save(rendezVous));
    }

    private RendezVousDto close(UUID id, StatusRendezVous target) {
        RendezVous rendezVous = validator.validateActive(id);
        rendezVous.setStatus(target);
        rendezVous.getCreneau().setStatut(StatutCreneau.INDISPONIBLE);
        creneauRepository.save(rendezVous.getCreneau());
        return mapper.toDto(repository.save(rendezVous));
    }

    private void libererSiActif(RendezVous rendezVous) {
        if (validator.isActive(rendezVous)) {
            rendezVous.getCreneau().setStatut(StatutCreneau.DISPONIBLE);
            creneauRepository.save(rendezVous.getCreneau());
        }
    }
}
