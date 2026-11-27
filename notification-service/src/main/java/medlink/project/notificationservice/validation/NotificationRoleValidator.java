package medlink.project.notificationservice.validation;

import medlink.project.notificationservice.entity.RecipientType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class NotificationRoleValidator {

    public Set<RecipientType> allowedRecipientTypes(Authentication authentication) {
        EnumSet<RecipientType> allowedTypes = EnumSet.noneOf(RecipientType.class);
        authentication.getAuthorities().forEach(authority -> {
            switch (authority.getAuthority()) {
                case "ROLE_PATIENT" -> allowedTypes.add(RecipientType.PATIENT);
                case "ROLE_PRATICIEN" -> allowedTypes.add(RecipientType.PRATICIEN);
                case "ROLE_ADMIN" -> allowedTypes.add(RecipientType.ADMIN);
                default -> {
                    // Les autres authorities ne donnent aucun accès aux notifications.
                }
            }
        });
        if (allowedTypes.isEmpty()) {
            throw new AccessDeniedException("Rôle destinataire non autorisé.");
        }
        return Set.copyOf(allowedTypes);
    }
}
