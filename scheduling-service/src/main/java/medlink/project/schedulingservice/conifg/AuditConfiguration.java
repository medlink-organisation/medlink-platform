package medlink.project.schedulingservice.conifg;

import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.RendezVous;
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
                        "creneau", "Creneau", Creneau.class,
                        "creneaux_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("praticienId", "cliniqueId", "dateDebut", "dateFin", "statut")
                ),
                new AuditedEntityDescriptor(
                        "rendez-vous", "RendezVous", RendezVous.class,
                        "rendez_vous_aud", "id", AuditedEntityDescriptor.uuidId(),
                        Set.of("creneau", "patientId", "status", "type")
                )
        ));
    }
}
