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
import medlink.grpc.auth.v1.GetUserContactRequest;
import medlink.grpc.directory.v1.DirectoryGrpcServiceGrpc;
import medlink.grpc.directory.v1.GetClinicByIdRequest;
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
import io.grpc.StatusRuntimeException;
import java.util.HashMap;

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
    private final DirectoryGrpcServiceGrpc.DirectoryGrpcServiceBlockingStub directoryStub;

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
                .map(this::toResultDto);
    }

    @Override
    public DownloadedDocument downloadMine() {
        long patientId = authenticatedUserService.requirePatientId();
        DossierMedical dossier = dossierValidator.requireByPatientId(patientId);
        accessService.checkCanRead(dossier.getId());
        return pdfService.generate(enrich(dossier, null));
    }

    @Override
    public DownloadedDocument downloadForAppointment(UUID rendezVousId) {
        DossierMedical dossier = requireForAppointment(rendezVousId);
        return pdfService.generate(enrich(dossier, rendezVousId));
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
        Map<Long, String> cliniques = new HashMap<>();
        reports.stream().map(medlink.project.medicalrecordservice.entities.CompteRendu::getCliniqueId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .forEach(id -> lookupClinique(id).ifPresent(name -> cliniques.put(id, name)));
        dto.setComptesRendus(reports.stream().map(report -> {
            var reportDto = compteRenduMapper.toDto(report);
            var praticien = praticiens.get(report.getPraticienBusinessId());
            if (praticien != null) {
                reportDto.setPraticienNom(emptyToNull(praticien.getNom()));
                reportDto.setPraticienPrenom(emptyToNull(praticien.getPrenom()));
                reportDto.setSpecialiteNom(emptyToNull(praticien.getSpecialiteNom()));
            }
            reportDto.setCliniqueNom(cliniques.get(report.getCliniqueId()));
            return reportDto;
        }).toList());
        dto.setResultatsExamens(resultatRepository
                .findByDossierMedicalIdOrderByDateExamenDesc(dossier.getId())
                .stream().map(this::toResultDto).toList());
        dto.setDocumentsMedicaux(documentMedicalRepository
                .findByDossierMedicalId(dossier.getId(), Pageable.unpaged())
                .getContent().stream().map(documentMedicalMapper::toDto).toList());
        return dto;
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private java.util.Optional<String> lookupClinique(Long id) {
        try {
            String name = directoryStub.withDeadlineAfter(3, TimeUnit.SECONDS)
                    .getClinicById(GetClinicByIdRequest.newBuilder()
                            .setCliniqueId(id)
                            .build())
                    .getName();
            return java.util.Optional.ofNullable(emptyToNull(name));
        } catch (StatusRuntimeException ignored) {
            return java.util.Optional.empty();
        }
    }

    private ResultatExamenDto toResultDto(
            medlink.project.medicalrecordservice.entities.ResultatExamen result
    ) {
        ResultatExamenDto dto = resultatMapper.toDto(result);
        String author = result.getDocument() == null ? null : result.getDocument().getCreatedBy();
        if (author == null || author.isBlank() || "SYSTEM".equals(author)) {
            return dto;
        }
        try {
            String displayName = authStub.withDeadlineAfter(3, TimeUnit.SECONDS)
                    .getUserContact(GetUserContactRequest.newBuilder()
                            .setKeycloakId(author)
                            .build())
                    .getDisplayName();
            dto.setPraticienNomComplet(emptyToNull(displayName));
        } catch (StatusRuntimeException ignored) {
            // Le résultat reste disponible sans enrichissement d'identité.
        }
        return dto;
    }
}
