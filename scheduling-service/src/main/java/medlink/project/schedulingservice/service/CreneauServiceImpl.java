package medlink.project.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.GenerationCreneauxRequest;
import medlink.project.schedulingservice.dto.GenerationCreneauxResponse;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.mapper.CreneauMapper;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.validator.CreneauValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreneauServiceImpl implements CreneauService {

    private final CreneauRepository repository;
    private final CreneauMapper mapper;
    private final CreneauValidator validator;
    private final DirectoryAssignmentClient assignmentClient;

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
    public List<CreneauDto> getCalendar(
            Long praticienId,
            Long cliniqueId,
            LocalDateTime debut,
            LocalDateTime fin
    ) {
        validateCalendarRange(debut, fin);
        return repository.findCalendar(praticienId, cliniqueId, debut, fin)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public GenerationCreneauxResponse generate(GenerationCreneauxRequest request) {
        validateGeneration(request);
        assignmentClient.requireActiveAssignments(request.praticienId(), List.of(request.cliniqueId()));

        List<Creneau> created = new ArrayList<>();
        for (LocalDate date : request.dates().stream().sorted().toList()) {
            LocalDateTime cursor = date.atTime(request.heureDebut());
            LocalDateTime workEnd = date.atTime(request.heureFin());
            while (!cursor.plusMinutes(request.dureeCreneauMinutes()).isAfter(workEnd)) {
                LocalDateTime slotEnd = cursor.plusMinutes(request.dureeCreneauMinutes());
                if (validator.overlaps(request.praticienId(), cursor, slotEnd)) {
                    throw new BusinessException("Conflit le " + date + " de " + cursor.toLocalTime()
                            + " à " + slotEnd.toLocalTime() + " : un créneau, un rendez-vous ou une indisponibilité existe déjà.");
                }
                created.add(Creneau.builder()
                        .praticienId(request.praticienId())
                        .cliniqueId(request.cliniqueId())
                        .dateDebut(cursor)
                        .dateFin(slotEnd)
                        .statut(StatutCreneau.DISPONIBLE)
                        .build());
                cursor = slotEnd;
            }
        }

        List<CreneauDto> result;
        try {
            result = repository.saveAllAndFlush(created).stream().map(mapper::toDto).toList();
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException("Une génération concurrente incompatible a été détectée. Rechargez le calendrier.");
        }
        return new GenerationCreneauxResponse(result.size(), result);
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

    private void validateGeneration(GenerationCreneauxRequest request) {
        if (!request.heureFin().isAfter(request.heureDebut())) {
            throw new BusinessException("L'heure de fin doit être strictement postérieure à l'heure de début.");
        }
        if (new HashSet<>(request.dates()).size() != request.dates().size()) {
            throw new BusinessException("La liste contient une date sélectionnée plusieurs fois.");
        }
        request.dates().forEach(date -> {
            if (date.isBefore(LocalDate.now())) {
                throw new BusinessException("La date " + date + " précède le début d'activité autorisé.");
            }
        });
    }

    private void validateCalendarRange(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new BusinessException(
                    "La période du calendrier est invalide."
            );
        }
        if (debut.plusYears(1).isBefore(fin)) {
            throw new BusinessException(
                    "La période du calendrier ne peut pas dépasser un an."
            );
        }
    }
}
