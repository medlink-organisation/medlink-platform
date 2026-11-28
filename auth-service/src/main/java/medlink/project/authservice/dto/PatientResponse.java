package medlink.project.authservice.dto;

import org.keycloak.representations.idm.UserRepresentation;

public record PatientResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean emailVerified,
        long businessPatientId,
        String birthDate,
        String gender,
        String phone
) {

    public static PatientResponse from(UserRepresentation user) {
        return new PatientResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                Boolean.TRUE.equals(user.isEnabled()),
                Boolean.TRUE.equals(user.isEmailVerified()),
                0L,
                firstAttribute(user, "birthdate", "dateNaissance"),
                firstAttribute(user, "gender", "sexe"),
                firstAttribute(user, "phone_number", "phone", "telephone")
        );
    }

    public static PatientResponse from(UserRepresentation user, long businessPatientId) {
        PatientResponse patient = from(user);
        return new PatientResponse(
                patient.id(),
                patient.username(),
                patient.email(),
                patient.firstName(),
                patient.lastName(),
                patient.enabled(),
                patient.emailVerified(),
                businessPatientId,
                patient.birthDate(),
                patient.gender(),
                patient.phone()
        );
    }

    private static String firstAttribute(UserRepresentation user, String... names) {
        for (String name : names) {
            String value = user.firstAttribute(name);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
