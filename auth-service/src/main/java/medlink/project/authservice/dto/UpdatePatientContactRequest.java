package medlink.project.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdatePatientContactRequest(
        @Email(message = "Email invalide")
        @Size(max = 254, message = "L'email ne doit pas dépasser 254 caractères")
        String email,

        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
        String firstName,

        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String lastName
) {
}
