package medlink.project.authservice.validation;

import medlink.project.authservice.dto.UpdatePatientContactRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Component
public class PatientValidator {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    public String requireKeycloakId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw invalid("keycloakId obligatoire");
        }
        return keycloakId.trim();
    }

    public String requireEmail(String email) {
        if (email == null || email.isBlank()) {
            throw invalid("email obligatoire");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (normalizedEmail.length() > 254
                || !EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw invalid("email invalide");
        }
        return normalizedEmail;
    }

    public void validatePagination(int first, int max) {
        if (first < 0) {
            throw invalid("first doit être positif ou nul");
        }
        if (max < 1 || max > MAX_PAGE_SIZE) {
            throw invalid(
                    "max doit être compris entre 1 et " + MAX_PAGE_SIZE
            );
        }
    }

    public void validateContact(UpdatePatientContactRequest request) {
        if (request == null) {
            throw invalid("contact obligatoire");
        }
        if (request.email() == null
                && request.firstName() == null
                && request.lastName() == null) {
            throw invalid(
                    "Au moins une information de contact est obligatoire"
            );
        }

        if (request.email() != null) {
            requireEmail(request.email());
        }
        validateOptionalName(request.firstName(), "firstName");
        validateOptionalName(request.lastName(), "lastName");
    }

    private void validateOptionalName(String value, String fieldName) {
        if (value != null && (value.isBlank() || value.trim().length() > 100)) {
            throw invalid(fieldName + " invalide");
        }
    }

    private ResponseStatusException invalid(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
