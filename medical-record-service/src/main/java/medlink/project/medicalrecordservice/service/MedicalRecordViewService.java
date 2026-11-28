package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MedicalRecordViewService {

    DossierMedicalDto getMine();

    DossierMedicalDto getForAppointment(UUID rendezVousId);

    Page<ResultatExamenDto> getResultsForAppointment(
            UUID rendezVousId,
            Pageable pageable
    );

    DownloadedDocument downloadMine();

    DownloadedDocument downloadForAppointment(UUID rendezVousId);
}
