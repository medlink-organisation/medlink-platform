package medlink.project.notificationservice.config;

import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.notificationservice.entity.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class AuditConfiguration {

    @Bean
    AuditedEntityRegistry auditedEntityRegistry() {
        return new AuditedEntityRegistry(List.of(
                new AuditedEntityDescriptor(
                        "notification", "Notification", Notification.class,
                        "notifications_aud", "id", AuditedEntityDescriptor.uuidId(),
                        Set.of(
                                "recipientType", "notificationType", "read", "readAt",
                                "emailSent", "correlationId"
                        )
                )
        ));
    }
}
