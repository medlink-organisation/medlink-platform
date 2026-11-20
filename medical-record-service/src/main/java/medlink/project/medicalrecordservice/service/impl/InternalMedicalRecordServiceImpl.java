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
    public CompteRenduDto saveCompteRendu(
            Long dossierId,
            UUID rendezVousId,
            UUID praticienId,
            String contenu
    ) {
        DossierMedical dossier = compteRenduValidator.validateDossierExists(dossierId);
        compteRenduValidator.validatePraticienIsProvided(praticienId);
        if (rendezVousId == null) {
            throw new BusinessValidationException("L'identifiant du rendez-vous est obligatoire.");
        }
        if (contenu == null || contenu.isBlank()) {
            throw new BusinessValidationException("Le contenu du compte rendu est obligatoire.");
        }

        return compteRenduRepository.findByRendezVousId(rendezVousId)
                .map(existing -> validateIdempotentRetry(existing, dossierId, praticienId, contenu))
                .orElseGet(() -> createCompteRendu(dossier, rendezVousId, praticienId, contenu));
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
            UUID rendezVousId,
            UUID praticienId,
            String contenu
    ) {
        CompteRendu entity = CompteRendu.builder()
                .dossierMedical(dossier)
                .rendezVousId(rendezVousId)
                .praticienId(praticienId)
                .contenu(contenu.trim())
                .build();
        return compteRenduMapper.toDto(compteRenduRepository.saveAndFlush(entity));
    }

    private CompteRenduDto validateIdempotentRetry(
            CompteRendu existing,
            Long dossierId,
            UUID praticienId,
            String contenu
    ) {
        if (!Objects.equals(existing.getDossierMedical().getId(), dossierId)
                || !Objects.equals(existing.getPraticienId(), praticienId)
                || !Objects.equals(existing.getContenu(), contenu.trim())) {
            throw new BusinessValidationException(
                    "Le rendez-vous possède déjà un compte rendu avec des données différentes."
            );
        }
        return compteRenduMapper.toDto(existing);
    }

    private void validatePatientId(Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new BusinessValidationException("L'identifiant du patient doit être positif.");
        }
    }
}
