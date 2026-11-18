package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentMedicalValidatorTest {

    @Mock
    private DocumentMedicalRepository documentMedicalRepository;

    @Mock
    private DossierMedicalRepository dossierMedicalRepository;

    @InjectMocks
    private DocumentMedicalValidator validator;

    @Test
    void rejectsDuplicateObjectKey() {
        String objectKey = "patients/42/document.pdf";
        when(documentMedicalRepository.existsByObjectKey(objectKey)).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> validator.validateObjectKeyUnique(objectKey)
        );
    }

    @Test
    void rejectsDocumentFromAnotherDossier() {
        DocumentMedical document = DocumentMedical.builder()
                .dossierMedical(DossierMedical.builder().id(5L).build())
                .build();

        assertThrows(
                BusinessValidationException.class,
                () -> validator.validateDocumentBelongsToDossier(document, 6L)
        );
    }
}
