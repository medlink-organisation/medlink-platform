package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.mapper.CompteRenduMapper;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.mapper.DocumentMedicalMapper;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.auth.v1.GetPatientsByBusinessIdsRequest;
import medlink.grpc.praticien.v1.GetPraticiensByIdsRequest;
import medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalRecordViewServiceImpl implements MedicalRecordViewService {

    private final MedicalRecordAccessService accessService;
    private final AuthenticatedUserService authenticatedUserService;
    private final DossierMedicalValidator dossierValidator;
    private final DossierMedicalRepository dossierRepository;
    private final ResultatExamenRepository resultatRepository;
    private final CompteRenduRepository compteRenduRepository;
    private final DocumentMedicalRepository documentMedicalRepository;
    private final DossierMedicalMapper dossierMapper;
    private final ResultatExamenMapper resultatMapper;
    private final CompteRenduMapper compteRenduMapper;
    private final DocumentMedicalMapper documentMedicalMapper;
    private final MedicalRecordPdfService pdfService;
    private final AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub authStub;
    private final PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienStub;

    @Override
    public DossierMedicalDto getMine() {
        long patientId = authenticatedUserService.requirePatientId();
        DossierMedical dossier = dossierValidator.requireByPatientId(patientId);
        accessService.checkCanRead(dossier.getId());
        return enrich(dossier, null);
    }

    @Override
    public DossierMedicalDto getForAppointment(UUID rendezVousId) {
        return enrich(requireForAppointment(rendezVousId), rendezVousId);
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

    private DossierMedicalDto enrich(DossierMedical dossier, UUID authorizedAppointmentId) {
        DossierMedicalDto dto = dossierMapper.toDto(dossier);
        var patients = authStub.withDeadlineAfter(4, TimeUnit.SECONDS)
                .getPatientsByBusinessIds(GetPatientsByBusinessIdsRequest.newBuilder()
                        .addBusinessPatientIds(dossier.getPatientId())
                        .build());
        patients.getPatientsList().stream().findFirst().ifPresent(patient -> {
            dto.setPatientNom(emptyToNull(patient.getLastName()));
            dto.setPatientPrenom(emptyToNull(patient.getFirstName()));
            dto.setPatientEmail(emptyToNull(patient.getEmail()));
            dto.setPatientTelephone(patient.hasPhone() ? patient.getPhone() : null);
            dto.setPatientDateNaissance(patient.hasBirthDate() ? patient.getBirthDate() : null);
            dto.setPatientSexe(patient.hasGender() ? patient.getGender() : null);
        });

        List<medlink.project.medicalrecordservice.entities.CompteRendu> reports =
                compteRenduRepository.findByDossierMedicalIdOrderByDateRedactionDesc(dossier.getId());
        if (authorizedAppointmentId != null) {
            String keycloakId = authenticatedUserService.requireAuthenticatedJwt()
                    .getToken().getSubject();
            long currentPraticienId = praticienStub.withDeadlineAfter(4, TimeUnit.SECONDS)
                    .getPraticienByKeycloakId(GetPraticienByKeycloakIdRequest.newBuilder()
                            .setKeycloakId(keycloakId)
                            .build())
                    .getId();
            reports = reports.stream()
                    .filter(report -> java.util.Objects.equals(
                            currentPraticienId, report.getPraticienBusinessId()))
                    .toList();
        }
        List<Long> praticienIds = reports.stream()
                .map(medlink.project.medicalrecordservice.entities.CompteRendu::getPraticienBusinessId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, medlink.grpc.praticien.v1.PraticienGrpcResponse> praticiens = praticienIds.isEmpty()
                ? Map.of()
                : praticienStub.withDeadlineAfter(4, TimeUnit.SECONDS)
                        .getPraticiensByIds(GetPraticiensByIdsRequest.newBuilder()
                                .addAllPraticienIds(praticienIds)
                                .build())
                        .getPraticiensList().stream()
                        .collect(Collectors.toMap(
                                medlink.grpc.praticien.v1.PraticienGrpcResponse::getId,
                                Function.identity()));
        dto.setComptesRendus(reports.stream().map(report -> {
            var reportDto = compteRenduMapper.toDto(report);
            var praticien = praticiens.get(report.getPraticienBusinessId());
            if (praticien != null) {
                reportDto.setPraticienNom(emptyToNull(praticien.getNom()));
                reportDto.setPraticienPrenom(emptyToNull(praticien.getPrenom()));
            }
            return reportDto;
        }).toList());
        dto.setResultatsExamens(resultatRepository
                .findByDossierMedicalIdOrderByDateExamenDesc(dossier.getId())
                .stream().map(resultatMapper::toDto).toList());
        dto.setDocumentsMedicaux(documentMedicalRepository
                .findByDossierMedicalId(dossier.getId(), Pageable.unpaged())
                .getContent().stream().map(documentMedicalMapper::toDto).toList());
        return dto;
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
