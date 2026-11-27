package medlink.project.notificationservice.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("notificationProducerValidator")
public class NotificationProducerValidator {

    private final String trustedClientId;

    public NotificationProducerValidator(
            @Value("${medlink.notification.security.trusted-producer-client-id:workflow-service}")
            String trustedClientId
    ) {
        this.trustedClientId = trustedClientId;
    }

    public boolean isAuthorized(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)
                || !authentication.isAuthenticated()) {
            return false;
        }

        String authorizedParty = jwtAuthentication.getToken().getClaimAsString("azp");
        if (authorizedParty == null || authorizedParty.isBlank()) {
            authorizedParty = jwtAuthentication.getToken().getClaimAsString("client_id");
        }
        return trustedClientId.equals(authorizedParty);
    }
}
