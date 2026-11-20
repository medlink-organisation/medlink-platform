package medlink.project.authservice.dto;

import org.keycloak.representations.idm.UserRepresentation;

public record PatientResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean emailVerified
) {

    public static PatientResponse from(UserRepresentation user) {
        return new PatientResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                Boolean.TRUE.equals(user.isEnabled()),
                Boolean.TRUE.equals(user.isEmailVerified())
        );
    }
}
