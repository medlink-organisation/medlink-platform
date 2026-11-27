package medlink.project.schedulingservice.service;

import medlink.project.schedulingservice.dto.RendezVousDto;
import medlink.project.schedulingservice.dto.RendezVousAgendaDto;
import medlink.project.schedulingservice.dto.PraticienPatientDto;
import medlink.project.schedulingservice.dto.PatientRendezVousDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousService {

    RendezVousDto create(RendezVousDto dto);

    RendezVousDto update(UUID id, RendezVousDto dto);

    RendezVousDto getById(UUID id);

    Page<RendezVousDto> getAll(Pageable pageable);

    Page<RendezVousDto> getByPatient(Long patientId, Pageable pageable);

    Page<PatientRendezVousDto> getForPatient(Long patientId, Pageable pageable);

    Page<RendezVousDto> getByCreneau(Long creneauId, Pageable pageable);

    List<RendezVousAgendaDto> getAgendaByPraticien(
            Long praticienId,
            LocalDateTime debut,
            LocalDateTime fin
    );

    List<RendezVousAgendaDto> getDueByPraticien(
            Long praticienId,
            int minutesBefore,
            int minutesAfter
    );

    Page<PraticienPatientDto> getPatientsByPraticien(
            Long praticienId,
            Pageable pageable
    );

    void delete(UUID id);

    RendezVousDto reserver(RendezVousDto dto);

    RendezVousDto confirmer(UUID id);

    RendezVousDto annuler(UUID id);

    RendezVousDto reporter(UUID id, Long nouveauCreneauId);

    RendezVousDto marquerAbsent(UUID id);

    RendezVousDto terminer(UUID id);
}
