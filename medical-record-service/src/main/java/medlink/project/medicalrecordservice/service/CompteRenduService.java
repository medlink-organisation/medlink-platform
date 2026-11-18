package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompteRenduService {

    CompteRenduDto create(CompteRenduDto dto);

    CompteRenduDto update(UUID id, CompteRenduDto dto);

    CompteRenduDto getById(UUID id);

    Page<CompteRenduDto> getByDossier(Long dossierId, Pageable pageable);

    Page<CompteRenduDto> getByPraticien(UUID praticienId, Pageable pageable);

    void delete(UUID id);
}
