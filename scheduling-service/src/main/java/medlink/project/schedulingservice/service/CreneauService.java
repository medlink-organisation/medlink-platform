package medlink.project.schedulingservice.service;

import medlink.project.schedulingservice.dto.CreneauDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CreneauService {

    CreneauDto create(CreneauDto dto);

    CreneauDto update(Long id, CreneauDto dto);

    CreneauDto getById(Long id);

    Page<CreneauDto> getAll(Pageable pageable);

    Page<CreneauDto> getByPraticien(Long praticienId, Pageable pageable);

    void delete(Long id);

    boolean isDisponible(Long id);
}
