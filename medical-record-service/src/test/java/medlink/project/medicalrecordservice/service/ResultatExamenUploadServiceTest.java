package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.ObjectStorageException;
import medlink.project.medicalrecordservice.exception.OrphanedObjectCleanupException;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import medlink.project.medicalrecordservice.service.impl.ResultatExamenPersistenceService;
import medlink.project.medicalrecordservice.service.impl.ResultatExamenServiceImpl;
import medlink.project.medicalrecordservice.storage.ObjectStorageService;
import medlink.project.medicalrecordservice.validator.FileSecurityValidator;
import medlink.project.medicalrecordservice.validator.ResultatExamenValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultatExamenUploadServiceTest {

    @Mock
    private ResultatExamenRepository repository;
    @Mock
    private ResultatExamenValidator validator;
    @Mock
    private ResultatExamenMapper mapper;
    @Mock
    private FileSecurityValidator fileSecurityValidator;
    @Mock
    private ClamAvService clamAvService;
    @Mock
    private Sha256ChecksumService checksumService;
    @Mock
    private ObjectStorageService objectStorageService;
    @Mock
    private ResultatExamenPersistenceService persistenceService;
    @Mock
    private AuthenticatedUserService authenticatedUserService;
    @Mock
    private MedicalRecordAccessService accessService;

    private ResultatExamenServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ResultatExamenServiceImpl(
                repository,
                validator,
                mapper,
                fileSecurityValidator,
                clamAvService,
                checksumService,
                objectStorageService,
                persistenceService,
                authenticatedUserService,
                accessService
        );
    }

    @Test
    void compensatesMinioWhenDatabasePersistenceFails() {
        stubValidatedFile();
        MockMultipartFile file = file();
        DossierMedical dossier = DossierMedical.builder().id(10L).build();
        when(validator.validateDossierExists(10L)).thenReturn(dossier);
        when(checksumService.calculate(any(InputStream.class))).thenReturn("a".repeat(64));
        when(objectStorageService.upload(any(InputStream.class), eq(file.getSize()), eq("application/pdf")))
                .thenReturn("medical-documents/random.pdf");
        when(persistenceService.persistUploadedResult(
                eq(dossier),
                eq("Analyse"),
                any(LocalDate.class),
                eq(null),
                eq("result.pdf"),
                eq(file.getSize()),
                eq("a".repeat(64)),
                eq("medical-documents/random.pdf")
        )).thenThrow(new IllegalStateException("database unavailable"));

        assertThrows(
                IllegalStateException.class,
                () -> service.uploadResultat(10L, "Analyse", LocalDate.now(), null, file)
        );

        verify(objectStorageService).delete("medical-documents/random.pdf");
    }

    @Test
    void doesNotPersistWhenMinioUploadFails() {
        stubValidatedFile();
        MockMultipartFile file = file();
        when(validator.validateDossierExists(10L)).thenReturn(DossierMedical.builder().id(10L).build());
        when(checksumService.calculate(any(InputStream.class))).thenReturn("a".repeat(64));
        when(objectStorageService.upload(any(InputStream.class), anyLong(), eq("application/pdf")))
                .thenThrow(new ObjectStorageException("MinIO unavailable", new RuntimeException()));

        assertThrows(
                ObjectStorageException.class,
                () -> service.uploadResultat(10L, "Analyse", LocalDate.now(), null, file)
        );

        verify(persistenceService, never()).persistUploadedResult(
                any(), any(), any(), any(), any(), anyLong(), any(), any()
        );
    }

    @Test
    void reportsOrphanWhenDatabaseAndMinioCompensationFail() {
        stubValidatedFile();
        MockMultipartFile file = file();
        DossierMedical dossier = DossierMedical.builder().id(10L).build();
        when(validator.validateDossierExists(10L)).thenReturn(dossier);
        when(checksumService.calculate(any(InputStream.class))).thenReturn("a".repeat(64));
        when(objectStorageService.upload(any(InputStream.class), eq(file.getSize()), eq("application/pdf")))
                .thenReturn("medical-documents/orphan.pdf");
        when(persistenceService.persistUploadedResult(
                eq(dossier), any(), any(), any(), any(), anyLong(), any(), any()
        )).thenThrow(new IllegalStateException("database unavailable"));
        doThrow(new ObjectStorageException("cleanup unavailable", new RuntimeException()))
                .when(objectStorageService).delete("medical-documents/orphan.pdf");

        assertThrows(
                OrphanedObjectCleanupException.class,
                () -> service.uploadResultat(10L, "Analyse", LocalDate.now(), null, file)
        );
    }

    @Test
    void rejectsIdorBeforeLoadingDossier() {
        doThrow(new AccessDeniedException("forbidden")).when(accessService).checkCanWrite(10L);

        assertThrows(
                AccessDeniedException.class,
                () -> service.uploadResultat(10L, "Analyse", LocalDate.now(), null, file())
        );

        verify(validator, never()).validateDossierExists(10L);
        verify(objectStorageService, never()).upload(any(), anyLong(), any());
    }

    @Test
    void rejectsDownloadForAnotherPractitionerBeforeMinioAccess() {
        UUID resultId = UUID.randomUUID();
        DossierMedical dossier = DossierMedical.builder().id(10L).build();
        ResultatExamen resultat = ResultatExamen.builder()
                .id(resultId)
                .dossierMedical(dossier)
                .document(DocumentMedical.builder().id(1L).dossierMedical(dossier).build())
                .build();
        when(validator.requireExists(resultId)).thenReturn(resultat);
        doThrow(new AccessDeniedException("forbidden")).when(accessService).checkCanRead(10L);

        assertThrows(AccessDeniedException.class, () -> service.download(resultId));

        verify(objectStorageService, never()).download(any());
    }

    private MockMultipartFile file() {
        return new MockMultipartFile(
                "file",
                "result.pdf",
                "application/pdf",
                "test-pdf".getBytes()
        );
    }

    private void stubValidatedFile() {
        when(fileSecurityValidator.validateAndReadPdf(any(MockMultipartFile.class)))
                .thenAnswer(invocation -> invocation.<MockMultipartFile>getArgument(0).getBytes());
    }
}
