package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultatExamenValidatorTest {

    @Mock
    private ResultatExamenRepository resultatExamenRepository;

    @Mock
    private DossierMedicalRepository dossierMedicalRepository;

    @InjectMocks
    private ResultatExamenValidator validator;

    @Test
    void rejectsExamenFromAnotherDossier() {
        UUID examenId = UUID.randomUUID();
        DossierMedical owner = DossierMedical.builder().id(10L).build();
        ResultatExamen examen = ResultatExamen.builder()
                .id(examenId)
                .dossierMedical(owner)
                .build();
        when(resultatExamenRepository.findById(examenId)).thenReturn(Optional.of(examen));

        assertThrows(
                BusinessValidationException.class,
                () -> validator.validateExamenBelongsToDossier(examenId, 11L)
        );
    }
}
