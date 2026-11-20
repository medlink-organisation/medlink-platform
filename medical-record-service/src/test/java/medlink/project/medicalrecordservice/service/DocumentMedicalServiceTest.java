package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.DocumentMedicalDto;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.mapper.DocumentMedicalMapper;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.security.authorization.MedicalRecordAccessService;
import medlink.project.medicalrecordservice.service.impl.DocumentMedicalServiceImpl;
import medlink.project.medicalrecordservice.validator.DocumentMedicalValidator;
import medlink.project.medicalrecordservice.validator.ResultatExamenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentMedicalServiceTest {

    @Test
    void returnsEmptyContentWhenSingleDocumentIsOutsideRequestedPage() {
        ResultatExamenValidator resultatValidator = mock(ResultatExamenValidator.class);
        DocumentMedicalMapper mapper = mock(DocumentMedicalMapper.class);
        UUID examenId = UUID.randomUUID();
        DossierMedical dossier = DossierMedical.builder().id(1L).build();
        DocumentMedical document = DocumentMedical.builder().id(2L).dossierMedical(dossier).build();
        ResultatExamen resultat = ResultatExamen.builder()
                .id(examenId)
                .dossierMedical(dossier)
                .document(document)
                .build();
        when(resultatValidator.requireExists(examenId)).thenReturn(resultat);
        when(mapper.toDto(document)).thenReturn(DocumentMedicalDto.builder().id(2L).build());
        DocumentMedicalServiceImpl service = new DocumentMedicalServiceImpl(
                mock(DocumentMedicalRepository.class),
                mock(DocumentMedicalValidator.class),
                resultatValidator,
                mapper,
                mock(AuthenticatedUserService.class),
                mock(MedicalRecordAccessService.class)
        );

        var page = service.getByExamen(examenId, PageRequest.of(1, 1));

        assertTrue(page.getContent().isEmpty());
        assertEquals(1, page.getTotalElements());
    }
}
