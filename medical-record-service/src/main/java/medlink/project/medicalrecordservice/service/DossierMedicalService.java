package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DossierMedicalService {

    DossierMedicalDto create(DossierMedicalDto dto);

    DossierMedicalDto update(Long id, DossierMedicalDto dto);

    DossierMedicalDto getById(Long id);

    DossierMedicalDto getByPatientId(Long patientId);

    Page<DossierMedicalDto> getAll(Pageable pageable);

    void delete(Long id);
}
