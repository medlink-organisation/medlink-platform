package medlink.project.praticienservice;

import medlink.project.auditcore.revision.AuditRevisionEntity;
import medlink.project.praticienservice.entity.Specialite;
import medlink.project.praticienservice.repository.SpecialiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EntityScan(basePackageClasses = {PraticienServiceApplication.class, AuditRevisionEntity.class})
public class PraticienServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PraticienServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PraticienServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initializeSpecialties(SpecialiteRepository specialiteRepository) {
        return args -> {
            List<Specialite> specialites = List.of(
                    new Specialite(
                            null,
                            "MED-GEN",
                            "Médecine générale",
                            "Consultations médicales générales, prévention et suivi médical des patients.",
                            true
                    ),
                    new Specialite(
                            null,
                            "CARDIO",
                            "Cardiologie",
                            "Diagnostic, traitement et suivi des maladies cardiovasculaires.",
                            true
                    ),
                    new Specialite(
                            null,
                            "PED",
                            "Pédiatrie",
                            "Consultations, prévention et suivi médical des nourrissons, enfants et adolescents.",
                            true
                    )
            );

            specialites.forEach(specialite -> {
                boolean exists = specialiteRepository.existsByNomIgnoreCase(specialite.getNom())
                        || specialiteRepository.existsByCodeIgnoreCase(specialite.getCode());

                if (exists) {
                    LOGGER.info("Specialty already exists: {}", specialite.getNom());
                    return;
                }

                specialiteRepository.save(specialite);
                LOGGER.info("Specialty created: {}", specialite.getNom());
            });
        };
    }
}
