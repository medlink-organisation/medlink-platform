package medlink.project.medicalrecordservice.config;

import medlink.project.auditcore.model.AuditedEntityDescriptor;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
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
                        "dossier-medical", "DossierMedical", DossierMedical.class,
                        "dossiers_medicaux_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("dateCreation", "derniereModification")
                ),
                new AuditedEntityDescriptor(
                        "document-medical", "DocumentMedical", DocumentMedical.class,
                        "documents_medicaux_aud", "id", AuditedEntityDescriptor.longId(),
                        Set.of("typeMime", "tailleOctets", "dateDepot")
                ),
                new AuditedEntityDescriptor(
                        "resultat-examen", "ResultatExamen", ResultatExamen.class,
                        "resultats_examens_aud", "id", AuditedEntityDescriptor.uuidId(),
                        Set.of("dateExamen", "datePublication")
                ),
                new AuditedEntityDescriptor(
                        "compte-rendu", "CompteRendu", CompteRendu.class,
                        "comptes_rendus_aud", "id", AuditedEntityDescriptor.uuidId(),
                        Set.of("dateRedaction")
                )
        ));
    }
}
