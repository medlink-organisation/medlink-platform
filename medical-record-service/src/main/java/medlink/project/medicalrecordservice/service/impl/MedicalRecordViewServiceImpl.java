package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.service.DownloadedDocument;
import medlink.project.medicalrecordservice.service.MedicalRecordPdfService;
import medlink.project.medicalrecordservice.service.MedicalRecordViewService;
import medlink.project.medicalrecordservice.validator.DossierMedicalValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordViewServiceImpl implements MedicalRecordViewService {

    private final MedicalRecordAccessService accessService;
    private final AuthenticatedUserService authenticatedUserService;
    private final DossierMedicalValidator dossierValidator;
    private final DossierMedicalRepository dossierRepository;
    private final ResultatExamenRepository resultatRepository;
    private final DossierMedicalMapper dossierMapper;
    private final ResultatExamenMapper resultatMapper;
    private final MedicalRecordPdfService pdfService;

    @Override
    public DossierMedicalDto getForAppointment(UUID rendezVousId) {
        return dossierMapper.toDto(requireForAppointment(rendezVousId));
    }

    @Override
    public Page<ResultatExamenDto> getResultsForAppointment(
            UUID rendezVousId,
            Pageable pageable
    ) {
        DossierMedical dossier = requireForAppointment(rendezVousId);
        return resultatRepository.findByDossierMedicalId(dossier.getId(), pageable)
                .map(resultatMapper::toDto);
    }

    @Override
    public DownloadedDocument downloadMine() {
        long patientId = authenticatedUserService.requirePatientId();
        DossierMedical dossier = dossierValidator.requireByPatientId(patientId);
        accessService.checkCanRead(dossier.getId());
        return pdfService.generate(dossier);
    }

    @Override
    public DownloadedDocument downloadForAppointment(UUID rendezVousId) {
        return pdfService.generate(requireForAppointment(rendezVousId));
    }

    private DossierMedical requireForAppointment(UUID rendezVousId) {
        Long dossierId = accessService.requireRecordIdForAppointment(rendezVousId);
        return dossierRepository.findById(dossierId)
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException(
                        "Acces au dossier medical refuse."
                ));
    }
}
