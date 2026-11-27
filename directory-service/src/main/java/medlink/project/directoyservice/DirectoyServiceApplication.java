package medlink.project.directoyservice;

import medlink.project.auditcore.revision.AuditRevisionEntity;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;

@SpringBootApplication
@EntityScan(basePackageClasses = {DirectoyServiceApplication.class, AuditRevisionEntity.class})
public class DirectoyServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoyServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DirectoyServiceApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(
            name = "medilink.seed.enabled",
            havingValue = "true",
            matchIfMissing = false
    )
    CommandLineRunner initializeReferenceData(
            CliniqueRepository cliniqueRepository,
            PlatformTransactionManager transactionManager
    ) {
        return args -> new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            LOGGER.info("[MEDLINK-SEED] Starting reference data initialization");

            if (cliniqueRepository.findByNomIgnoreCase("Clinique Internationale de Sousse").isEmpty()) {
                Clinique clinique = new Clinique();
                clinique.setNom("Clinique Internationale de Sousse");
                clinique.setAdresse(null);
                clinique.setTelephone(null);
                clinique.setEmail(null);
                clinique.setVille("Sousse");
                clinique.setBlocs(new ArrayList<>());
                clinique.setServices(new ArrayList<>());
                cliniqueRepository.saveAndFlush(clinique);
                LOGGER.info("[MEDLINK-SEED] Clinic CIS created");
            } else {
                LOGGER.info("[MEDLINK-SEED] Clinic CIS already exists - skipped");
            }

            if (cliniqueRepository.findByNomIgnoreCase("Clinique Médicale de Sfax").isEmpty()) {
                Clinique clinique = new Clinique();
                clinique.setNom("Clinique Médicale de Sfax");
                clinique.setAdresse(null);
                clinique.setTelephone(null);
                clinique.setEmail(null);
                clinique.setVille("Sfax");
                clinique.setBlocs(new ArrayList<>());
                clinique.setServices(new ArrayList<>());
                cliniqueRepository.saveAndFlush(clinique);
                LOGGER.info("[MEDLINK-SEED] Clinic CMS created");
            } else {
                LOGGER.info("[MEDLINK-SEED] Clinic CMS already exists - skipped");
            }

            if (cliniqueRepository.findByNomIgnoreCase("Clinique Les Jardins de Tunis").isEmpty()) {
                Clinique clinique = new Clinique();
                clinique.setNom("Clinique Les Jardins de Tunis");
                clinique.setAdresse(null);
                clinique.setTelephone(null);
                clinique.setEmail(null);
                clinique.setVille("Tunis");
                clinique.setBlocs(new ArrayList<>());
                clinique.setServices(new ArrayList<>());
                cliniqueRepository.saveAndFlush(clinique);
                LOGGER.info("[MEDLINK-SEED] Clinic CJT created");
            } else {
                LOGGER.info("[MEDLINK-SEED] Clinic CJT already exists - skipped");
            }

            LOGGER.info("[MEDLINK-SEED] Reference data initialization completed");
        });
    }
}
