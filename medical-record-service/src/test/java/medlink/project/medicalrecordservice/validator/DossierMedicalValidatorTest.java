package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.exception.ResourceAlreadyExistsException;
import medlink.project.medicalrecordservice.exception.ResourceNotFoundException;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DossierMedicalValidatorTest {

    @Mock
    private DossierMedicalRepository repository;

    @InjectMocks
    private DossierMedicalValidator validator;

    @Test
    void rejectsPatientWhoAlreadyHasDossier() {
        when(repository.existsByPatientId(42L)).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> validator.validatePatientNotAlreadyHasDossier(42L)
        );
    }

    @Test
    void requiresExistingDossier() {
        DossierMedical dossier = DossierMedical.builder().id(15L).patientId(42L).build();
        when(repository.findById(15L)).thenReturn(Optional.of(dossier));

        assertSame(dossier, validator.requireExists(15L));
    }

    @Test
    void rejectsUnknownDossier() {
        when(repository.findById(15L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> validator.requireExists(15L));
    }
}
