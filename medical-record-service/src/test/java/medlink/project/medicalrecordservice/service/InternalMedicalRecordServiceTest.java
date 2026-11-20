package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.CompteRenduMapper;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.service.impl.InternalMedicalRecordServiceImpl;
import medlink.project.medicalrecordservice.validator.CompteRenduValidator;
import medlink.project.medicalrecordservice.validator.DossierMedicalValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternalMedicalRecordServiceTest {

    @Mock
    private DossierMedicalRepository dossierRepository;
    @Mock
    private CompteRenduRepository compteRenduRepository;
    @Mock
    private DossierMedicalValidator dossierValidator;
    @Mock
    private CompteRenduValidator compteRenduValidator;
    @Mock
    private DossierMedicalMapper dossierMapper;
    @Mock
    private CompteRenduMapper compteRenduMapper;

    private InternalMedicalRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new InternalMedicalRecordServiceImpl(
                dossierRepository,
                compteRenduRepository,
                dossierValidator,
                compteRenduValidator,
                dossierMapper,
                compteRenduMapper
        );
    }

    @Test
    void ensureDossierIsIdempotent() {
        DossierMedical existing = DossierMedical.builder().id(7L).patientId(42L).build();
        DossierMedicalDto expected = DossierMedicalDto.builder().id(7L).patientId(42L).build();
        when(dossierRepository.findByPatientId(42L)).thenReturn(Optional.of(existing));
        when(dossierMapper.toDto(existing)).thenReturn(expected);

        assertSame(expected, service.ensureDossierForPatient(42L));
        verify(dossierRepository, never()).saveAndFlush(any());
    }

    @Test
    void compteRenduRetryReturnsExistingResource() {
        UUID rendezVousId = UUID.randomUUID();
        UUID praticienId = UUID.randomUUID();
        DossierMedical dossier = DossierMedical.builder().id(7L).build();
        CompteRendu existing = CompteRendu.builder()
                .dossierMedical(dossier)
                .rendezVousId(rendezVousId)
                .praticienId(praticienId)
                .contenu("Consultation terminée")
                .build();
        CompteRenduDto expected = CompteRenduDto.builder().rendezVousId(rendezVousId).build();
        when(compteRenduValidator.validateDossierExists(7L)).thenReturn(dossier);
        when(compteRenduRepository.findByRendezVousId(rendezVousId))
                .thenReturn(Optional.of(existing));
        when(compteRenduMapper.toDto(existing)).thenReturn(expected);

        assertSame(
                expected,
                service.saveCompteRendu(
                        7L,
                        rendezVousId,
                        praticienId,
                        "Consultation terminée"
                )
        );
        verify(compteRenduRepository, never()).saveAndFlush(any());
    }

    @Test
    void compteRenduRetryRejectsDifferentPayload() {
        UUID rendezVousId = UUID.randomUUID();
        UUID praticienId = UUID.randomUUID();
        DossierMedical dossier = DossierMedical.builder().id(7L).build();
        CompteRendu existing = CompteRendu.builder()
                .dossierMedical(dossier)
                .rendezVousId(rendezVousId)
                .praticienId(praticienId)
                .contenu("Premier contenu")
                .build();
        when(compteRenduValidator.validateDossierExists(7L)).thenReturn(dossier);
        when(compteRenduRepository.findByRendezVousId(rendezVousId))
                .thenReturn(Optional.of(existing));

        assertThrows(
                BusinessValidationException.class,
                () -> service.saveCompteRendu(7L, rendezVousId, praticienId, "Contenu modifié")
        );
        verify(compteRenduRepository, never()).saveAndFlush(any());
    }
}
