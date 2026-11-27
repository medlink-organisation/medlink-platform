package medlink.project.schedulingservice.service;

import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.dto.GenerationCreneauxRequest;
import medlink.project.schedulingservice.dto.GenerationCreneauxResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CreneauService {

    CreneauDto create(CreneauDto dto);

    CreneauDto update(Long id, CreneauDto dto);

    CreneauDto getById(Long id);

    Page<CreneauDto> getAll(Pageable pageable);

    Page<CreneauDto> getByPraticien(Long praticienId, Pageable pageable);

    List<CreneauDto> getCalendar(
            Long praticienId,
            Long cliniqueId,
            LocalDateTime debut,
            LocalDateTime fin
    );

    GenerationCreneauxResponse generate(GenerationCreneauxRequest request);

    void delete(Long id);

    boolean isDisponible(Long id);

    CreneauDto reserver(Long id);

    CreneauDto liberer(Long id);
}
