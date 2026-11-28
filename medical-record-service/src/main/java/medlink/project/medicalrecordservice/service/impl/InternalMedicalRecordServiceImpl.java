package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.CompteRenduMapper;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.service.InternalMedicalRecordService;
import medlink.project.medicalrecordservice.validator.CompteRenduValidator;
import medlink.project.medicalrecordservice.validator.DossierMedicalValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InternalMedicalRecordServiceImpl implements InternalMedicalRecordService {

    private final DossierMedicalRepository dossierRepository;
    private final CompteRenduRepository compteRenduRepository;
    private final DossierMedicalValidator dossierValidator;
    private final CompteRenduValidator compteRenduValidator;
    private final DossierMedicalMapper dossierMapper;
    private final CompteRenduMapper compteRenduMapper;

    @Override
    public DossierMedicalDto ensureDossierForPatient(Long patientId) {
        validatePatientId(patientId);
        return dossierRepository.findByPatientId(patientId)
                .map(dossierMapper::toDto)
                .orElseGet(() -> createDossierSafely(patientId));
    }

    @Override
    @Transactional(readOnly = true)
    public DossierMedicalDto getDossierByPatientId(Long patientId) {
        validatePatientId(patientId);
        return dossierMapper.toDto(dossierValidator.requireByPatientId(patientId));
    }

    @Override
    @Transactional
    public CompteRenduDto saveCompteRendu(ConsultationReportCommand command) {
        if (command == null) {
            throw new BusinessValidationException("Le compte rendu est obligatoire.");
        }
        DossierMedical dossier = dossierRepository.findByIdForUpdate(command.dossierId())
                .orElseThrow(() -> new BusinessValidationException("Dossier médical introuvable."));
        compteRenduValidator.validatePraticienIsProvided(command.praticienId());
        if (command.rendezVousId() == null) {
            throw new BusinessValidationException("L'identifiant du rendez-vous est obligatoire.");
        }
        if (command.cliniqueId() == null || command.cliniqueId() <= 0) {
            throw new BusinessValidationException("L'identifiant de la clinique est obligatoire.");
        }
        requireText(command.motif(), "Le motif de consultation est obligatoire.");
        requireText(command.diagnostic(), "Le diagnostic est obligatoire.");

        return compteRenduRepository.findByRendezVousId(command.rendezVousId())
                .map(existing -> validateIdempotentRetry(existing, command))
                .orElseGet(() -> createCompteRendu(dossier, command));
    }

    @Override
    @Transactional
    public CompteRenduDto saveCompteRendu(
            Long dossierId,
            UUID rendezVousId,
            UUID praticienId,
            String contenu
    ) {
        DossierMedical dossier = compteRenduValidator.validateDossierExists(dossierId);
        if (rendezVousId == null || praticienId == null || contenu == null || contenu.isBlank()) {
            throw new BusinessValidationException("Les données du compte rendu sont obligatoires.");
        }
        return compteRenduRepository.findByRendezVousId(rendezVousId)
                .map(existing -> {
                    if (!Objects.equals(existing.getDossierMedical().getId(), dossierId)
                            || !Objects.equals(existing.getPraticienId(), praticienId)
                            || !Objects.equals(existing.getContenu(), contenu.trim())) {
                        throw new BusinessValidationException(
                                "Le rendez-vous possède déjà un compte rendu avec des données différentes.");
                    }
                    return compteRenduMapper.toDto(existing);
                })
                .orElseGet(() -> compteRenduMapper.toDto(compteRenduRepository.saveAndFlush(
                        CompteRendu.builder()
                                .dossierMedical(dossier)
                                .rendezVousId(rendezVousId)
                                .praticienId(praticienId)
                                .contenu(contenu.trim())
                                .build())));
    }

    private DossierMedicalDto createDossierSafely(Long patientId) {
        try {
            DossierMedical dossier = dossierRepository.saveAndFlush(
                    DossierMedical.builder().patientId(patientId).build()
            );
            return dossierMapper.toDto(dossier);
        } catch (DataIntegrityViolationException concurrentCreation) {
            DossierMedical existing = dossierRepository.findByPatientId(patientId)
                    .orElseThrow(() -> concurrentCreation);
            return dossierMapper.toDto(existing);
        }
    }

    private CompteRenduDto createCompteRendu(
            DossierMedical dossier,
            ConsultationReportCommand command
    ) {
        CompteRendu entity = CompteRendu.builder()
                .dossierMedical(dossier)
                .rendezVousId(command.rendezVousId())
                .praticienBusinessId(command.praticienId())
                .cliniqueId(command.cliniqueId())
                .dateConsultation(command.dateConsultation())
                .motif(normalize(command.motif()))
                .symptomes(normalize(command.symptomes()))
                .diagnostic(normalize(command.diagnostic()))
                .observations(normalize(command.observations()))
                .traitementRecommande(normalize(command.traitementRecommande()))
                .examensComplementaires(normalize(command.examensComplementaires()))
                .recommandations(normalize(command.recommandations()))
                .controleRequis(command.controleRequis())
                .build();
        CompteRendu saved = compteRenduRepository.saveAndFlush(entity);
        dossier.setDerniereModification(LocalDateTime.now());
        dossierRepository.save(dossier);
        return compteRenduMapper.toDto(saved);
    }

    private CompteRenduDto validateIdempotentRetry(
            CompteRendu existing,
            ConsultationReportCommand command
    ) {
        if (!Objects.equals(existing.getDossierMedical().getId(), command.dossierId())
                || !Objects.equals(existing.getPraticienBusinessId(), command.praticienId())
                || !Objects.equals(existing.getCliniqueId(), command.cliniqueId())
                || !Objects.equals(existing.getDateConsultation(), command.dateConsultation())
                || !Objects.equals(existing.getMotif(), normalize(command.motif()))
                || !Objects.equals(existing.getSymptomes(), normalize(command.symptomes()))
                || !Objects.equals(existing.getDiagnostic(), normalize(command.diagnostic()))
                || !Objects.equals(existing.getObservations(), normalize(command.observations()))
                || !Objects.equals(existing.getTraitementRecommande(), normalize(command.traitementRecommande()))
                || !Objects.equals(existing.getExamensComplementaires(), normalize(command.examensComplementaires()))
                || !Objects.equals(existing.getRecommandations(), normalize(command.recommandations()))
                || !Objects.equals(existing.getControleRequis(), command.controleRequis())) {
            throw new BusinessValidationException(
                    "Le rendez-vous possède déjà un compte rendu avec des données différentes."
            );
        }
        return compteRenduMapper.toDto(existing);
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessValidationException(message);
        }
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void validatePatientId(Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new BusinessValidationException("L'identifiant du patient doit être positif.");
        }
    }
}
