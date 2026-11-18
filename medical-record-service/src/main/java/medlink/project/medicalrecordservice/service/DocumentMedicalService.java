package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.DocumentMedicalDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DocumentMedicalService {

    DocumentMedicalDto getById(Long id);

    Page<DocumentMedicalDto> getByDossier(Long dossierId, Pageable pageable);

    Page<DocumentMedicalDto> getByExamen(UUID examenId, Pageable pageable);
}
