package medlink.project.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.dto.RendezVousAgendaDto;
import medlink.project.schedulingservice.dto.PraticienPatientDto;
import medlink.project.schedulingservice.dto.PatientRendezVousDto;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.RendezVous;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.mapper.RendezVousMapper;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import medlink.project.schedulingservice.validator.RendezVousValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

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
    public Page<PatientRendezVousDto> getForPatient(Long patientId, Pageable pageable) {
        return repository.findAllByPatientId(patientId, pageable)
                .map(this::toPatientDto);
    }

    @Override
    public Page<RendezVousDto> getByCreneau(Long creneauId, Pageable pageable) {
        return repository.findAllByCreneauId(creneauId, pageable).map(mapper::toDto);
    }

    @Override
    public List<RendezVousAgendaDto> getAgendaByPraticien(
            Long praticienId,
            LocalDateTime debut,
            LocalDateTime fin
    ) {
        validateAgendaRange(debut, fin);
        return repository.findAgendaByPraticien(praticienId, debut, fin)
                .stream()
                .map(this::toAgendaDto)
                .toList();
    }

    @Override
    public List<RendezVousAgendaDto> getDueByPraticien(
            Long praticienId,
            int minutesBefore,
            int minutesAfter
    ) {
        if (minutesBefore < 0 || minutesBefore > 240
                || minutesAfter < 0 || minutesAfter > 480) {
            throw new BusinessException(
                    "La fenêtre de rappel est invalide."
            );
        }
        LocalDateTime now = LocalDateTime.now();
        return repository.findDueByPraticien(
                        praticienId,
                        List.of(StatusRendezVous.CONFIRME),
                        now.minusMinutes(minutesAfter),
                        now.plusMinutes(minutesBefore)
                ).stream()
                .map(this::toAgendaDto)
                .toList();
    }

    @Override
    public Page<PraticienPatientDto> getPatientsByPraticien(
            Long praticienId,
            Pageable pageable
    ) {
        return repository.findLatestCompletedByPraticien(
                praticienId,
                StatusRendezVous.TERMINE,
                pageable
        ).map(rendezVous -> new PraticienPatientDto(
                rendezVous.getPatientId(),
                rendezVous.getId(),
                rendezVous.getCreneau().getDateDebut(),
                rendezVous.getCreneau().getCliniqueId()
        ));
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
        try {
            return mapper.toDto(repository.saveAndFlush(rendezVous));
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(
                    "Le créneau vient d'être réservé par un autre patient.");
        }
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

    private RendezVousAgendaDto toAgendaDto(RendezVous rendezVous) {
        Creneau creneau = rendezVous.getCreneau();
        return new RendezVousAgendaDto(
                rendezVous.getId(),
                rendezVous.getPatientId(),
                creneau.getPraticienId(),
                creneau.getCliniqueId(),
                creneau.getId(),
                creneau.getDateDebut(),
                creneau.getDateFin(),
                rendezVous.getStatus(),
                rendezVous.getType()
        );
    }

    private PatientRendezVousDto toPatientDto(RendezVous rendezVous) {
        Creneau creneau = rendezVous.getCreneau();
        return new PatientRendezVousDto(
                rendezVous.getId(),
                creneau.getId(),
                creneau.getPraticienId(),
                creneau.getCliniqueId(),
                creneau.getDateDebut(),
                creneau.getDateFin(),
                rendezVous.getStatus(),
                rendezVous.getType()
        );
    }

    private void validateAgendaRange(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new BusinessException(
                    "La période de l'agenda est invalide."
            );
        }
        if (debut.plusYears(1).isBefore(fin)) {
            throw new BusinessException(
                    "La période de l'agenda ne peut pas dépasser un an."
            );
        }
    }
}
