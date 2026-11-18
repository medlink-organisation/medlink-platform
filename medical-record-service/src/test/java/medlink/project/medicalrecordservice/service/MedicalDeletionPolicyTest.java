package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.mapper.DossierMedicalMapper;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import medlink.project.medicalrecordservice.service.impl.DossierMedicalServiceImpl;
import medlink.project.medicalrecordservice.service.impl.ResultatExamenServiceImpl;
import medlink.project.medicalrecordservice.service.impl.ResultatExamenPersistenceService;
import medlink.project.medicalrecordservice.storage.ObjectStorageService;
import medlink.project.medicalrecordservice.validator.DossierMedicalValidator;
import medlink.project.medicalrecordservice.validator.FileSecurityValidator;
import medlink.project.medicalrecordservice.validator.ResultatExamenValidator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MedicalDeletionPolicyTest {

    @Test
    void refusesToDeleteDossierContainingMedicalData() {
        DossierMedicalRepository repository = mock(DossierMedicalRepository.class);
        DossierMedicalValidator validator = mock(DossierMedicalValidator.class);
        DossierMedical dossier = DossierMedical.builder()
                .id(1L)
                .documents(List.of(DocumentMedical.builder().id(1L).build()))
                .build();
        when(validator.requireExists(1L)).thenReturn(dossier);
        DossierMedicalServiceImpl service = new DossierMedicalServiceImpl(
                repository,
                validator,
                mock(DossierMedicalMapper.class),
                mock(AuthenticatedUserService.class),
                mock(MedicalRecordAccessService.class)
        );

        assertThrows(BusinessValidationException.class, () -> service.delete(1L));
        verify(repository, never()).delete(dossier);
    }

    @Test
    void refusesToDeletePublishedExamenWithoutConservationPolicy() {
        UUID examenId = UUID.randomUUID();
        ResultatExamenRepository repository = mock(ResultatExamenRepository.class);
        ResultatExamenValidator validator = mock(ResultatExamenValidator.class);
        ResultatExamen examen = ResultatExamen.builder()
                .id(examenId)
                .dossierMedical(DossierMedical.builder().id(1L).build())
                .document(DocumentMedical.builder().id(1L).build())
                .build();
        when(validator.requireExists(examenId)).thenReturn(examen);
        ResultatExamenServiceImpl service = new ResultatExamenServiceImpl(
                repository,
                validator,
                mock(ResultatExamenMapper.class),
                mock(FileSecurityValidator.class),
                mock(ClamAvService.class),
                mock(Sha256ChecksumService.class),
                mock(ObjectStorageService.class),
                mock(ResultatExamenPersistenceService.class),
                mock(AuthenticatedUserService.class),
                mock(MedicalRecordAccessService.class)
        );

        assertThrows(BusinessValidationException.class, () -> service.delete(examenId));
        verify(repository, never()).delete(examen);
    }
}
