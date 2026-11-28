package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

public interface ResultatExamenService {

    ResultatExamenDto uploadResultat(
            Long dossierId,
            UUID rendezVousId,
            String typeExamen,
            LocalDate dateExamen,
            String conclusion,
            MultipartFile file
    );

    ResultatExamenDto getById(UUID id);

    Page<ResultatExamenDto> getByDossier(Long dossierId, Pageable pageable);

    DownloadedDocument download(UUID id);

    DownloadedDocument downloadForAppointment(UUID rendezVousId, UUID id);

    void delete(UUID id);
}
