package medlink.project.authservice.validation;

import medlink.project.authservice.dto.UpdatePatientContactRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PatientValidatorTest {

    private final PatientValidator validator = new PatientValidator();

    @Test
    void normalizesEmail() {
        assertEquals(
                "patient@medlink.tn",
                validator.requireEmail(" Patient@MedLink.tn ")
        );
    }

    @Test
    void rejectsInvalidEmail() {
        assertBadRequest(() -> validator.requireEmail("patient"));
    }

    @Test
    void rejectsInvalidPagination() {
        assertBadRequest(() -> validator.validatePagination(-1, 20));
        assertBadRequest(() -> validator.validatePagination(0, 101));
    }

    @Test
    void rejectsEmptyContactUpdate() {
        assertBadRequest(() -> validator.validateContact(
                new UpdatePatientContactRequest(null, null, null)
        ));
    }

    private void assertBadRequest(Runnable action) {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                action::run
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
