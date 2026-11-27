package medlink.project.auditcore.revision;

import medlink.project.auditcore.dto.ActorType;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionObject) {
        AuditRevisionEntity revision = (AuditRevisionEntity) revisionObject;
        AuditActorContext.current().ifPresentOrElse(actor -> apply(revision, actor), () -> applySecurityContext(revision));
    }

    private void applySecurityContext(AuditRevisionEntity revision) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt && authentication.isAuthenticated()) {
            String subject = jwt.getToken().getSubject();
            String username = jwt.getToken().getClaimAsString("preferred_username");
            String clientId = firstNonBlank(
                    jwt.getToken().getClaimAsString("azp"),
                    jwt.getToken().getClaimAsString("client_id")
            );
            boolean serviceAccount = username != null && username.startsWith("service-account-");
            ActorType type = serviceAccount ? ActorType.SERVICE : ActorType.USER;
            apply(revision, new AuditActorContext.Actor(
                    subject,
                    firstNonBlank(username, clientId, subject),
                    type,
                    UUID.randomUUID().toString()
            ));
            return;
        }
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            apply(revision, new AuditActorContext.Actor(
                    "SYSTEM", "SYSTEM", ActorType.SYSTEM, UUID.randomUUID().toString()
            ));
            return;
        }
        apply(revision, new AuditActorContext.Actor(
                null, authentication.getName(), ActorType.UNKNOWN, UUID.randomUUID().toString()
        ));
    }

    private void apply(AuditRevisionEntity revision, AuditActorContext.Actor actor) {
        revision.setUserId(actor.userId());
        revision.setUsername(actor.username());
        revision.setActorType(actor.actorType() == null ? ActorType.UNKNOWN : actor.actorType());
        revision.setCorrelationId(firstNonBlank(actor.correlationId(), UUID.randomUUID().toString()));
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
