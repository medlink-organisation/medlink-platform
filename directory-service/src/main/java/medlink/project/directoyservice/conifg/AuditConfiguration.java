package medlink.project.directoyservice.conifg;

import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.entity.ServiceMedicale;
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
                        "affectation-praticien", "AffectationPraticien", AffectationPraticien.class,
                        "affectation_praticien_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("praticienId", "actif", "deleted", "dateAffectation", "clinique")
                ),
                new AuditedEntityDescriptor(
                        "bloc", "Bloc", Bloc.class,
                        "bloc_aud", "id_bloc", AuditedEntityDescriptor.longId(),
                        Set.of("code", "nom", "description", "clinique")
                ),
                new AuditedEntityDescriptor(
                        "clinique", "Clinique", Clinique.class,
                        "clinique_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("nom", "adresse", "telephone", "email", "ville")
                ),
                new AuditedEntityDescriptor(
                        "service-medicale", "ServiceMedicale", ServiceMedicale.class,
                        "service_medicale_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("code", "libelle", "description", "actif", "clinique", "bloc")
                )
        ));
    }
}
