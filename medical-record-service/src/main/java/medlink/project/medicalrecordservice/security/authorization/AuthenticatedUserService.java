package medlink.project.medicalrecordservice.security.authorization;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

    public JwtAuthenticationToken requireAuthenticatedJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)
                || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Un JWT Keycloak authentifié est requis."
            );
        }
        return jwtAuthentication;
    }

    public long requirePatientId() {
        Object claim = requireAuthenticatedJwt().getToken().getClaim("patient_id");
        long patientId;
        if (claim instanceof Number number) {
            patientId = number.longValue();
        } else if (claim instanceof String text) {
            try {
                patientId = Long.parseLong(text);
            } catch (NumberFormatException exception) {
                throw new AuthenticationCredentialsNotFoundException(
                        "Le claim patient_id du JWT est invalide."
                );
            }
        } else {
            throw new AuthenticationCredentialsNotFoundException(
                    "Le claim patient_id du JWT est obligatoire."
            );
        }
        if (patientId <= 0) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Le claim patient_id du JWT doit être positif."
            );
        }
        return patientId;
    }
}
