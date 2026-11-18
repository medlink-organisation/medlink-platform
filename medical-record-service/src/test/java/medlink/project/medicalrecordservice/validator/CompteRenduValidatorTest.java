package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompteRenduValidatorTest {

    @Mock
    private CompteRenduRepository compteRenduRepository;

    @Mock
    private DossierMedicalRepository dossierMedicalRepository;

    @InjectMocks
    private CompteRenduValidator validator;

    @Test
    void rejectsRendezVousAlreadyUsed() {
        UUID rendezVousId = UUID.randomUUID();
        when(compteRenduRepository.existsByRendezVousId(rendezVousId)).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> validator.validateRendezVousNotAlreadyUsed(rendezVousId)
        );
    }

    @Test
    void requiresPraticienIdentifier() {
        assertThrows(
                BusinessValidationException.class,
                () -> validator.validatePraticienIsProvided(null)
        );
    }
}
