package medlink.project.praticienservice.conifg;

import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.praticienservice.entity.Praticien;
import medlink.project.praticienservice.entity.Specialite;
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
                        "praticien", "Praticien", Praticien.class,
                        "praticien_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("keycloakId", "nom", "prenom", "email", "phone", "actif", "numeroOrdre", "specialite")
                ),
                new AuditedEntityDescriptor(
                        "specialite", "Specialite", Specialite.class,
                        "specialites_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("code", "nom", "description", "actif")
                )
        ));
    }
}
