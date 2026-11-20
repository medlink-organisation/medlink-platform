package medlink.project.directoyservice;

import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.entity.ServiceMedicale;
import medlink.project.directoyservice.repositories.AffectationPraticienRepository;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.repositories.ServiceMedicaleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@SpringBootApplication
public class DirectoyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectoyServiceApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(
            name = "medlink.directory.data-initialization-enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    CommandLineRunner initializeDirectoryData(
            CliniqueRepository cliniqueRepository,
            BlocRepository blocRepository,
            ServiceMedicaleRepository serviceRepository,
            AffectationPraticienRepository affectationRepository,
            TransactionTemplate transactionTemplate,
            @Value("${medlink.directory.demo-praticien-1-id:1}") Long praticien1Id,
            @Value("${medlink.directory.demo-praticien-2-id:2}") Long praticien2Id
    ) {
        return args -> transactionTemplate.executeWithoutResult(status ->
                initializeData(
                        cliniqueRepository,
                        blocRepository,
                        serviceRepository,
                        affectationRepository,
                        praticien1Id,
                        praticien2Id
                )
        );
    }

    void initializeData(
            CliniqueRepository cliniqueRepository,
            BlocRepository blocRepository,
            ServiceMedicaleRepository serviceRepository,
            AffectationPraticienRepository affectationRepository,
            Long praticien1Id,
            Long praticien2Id
    ) {
        Clinique cliniqueTunis = findOrCreateClinique(
                cliniqueRepository,
                "Clinique MedLink Tunis",
                "10 avenue Habib Bourguiba, Tunis",
                "+21671000001",
                "contact.tunis@clinique-medilink.tn",
                "Tunis"
        );

        Clinique cliniqueSousse = findOrCreateClinique(
                cliniqueRepository,
                "Clinique MedLink Sousse",
                "20 avenue Hédi Chaker, Sousse",
                "+21673000002",
                "contact.sousse@clinique-medilink.tn",
                "Sousse"
        );

        Bloc blocTunis = findOrCreateBloc(
                blocRepository,
                cliniqueTunis,
                "BLOC-A",
                "Bloc opératoire A",
                "Bloc opératoire principal de Tunis"
        );

        Bloc blocSousse = findOrCreateBloc(
                blocRepository,
                cliniqueSousse,
                "BLOC-B",
                "Bloc opératoire B",
                "Bloc opératoire principal de Sousse"
        );

        findOrCreateService(
                serviceRepository,
                cliniqueTunis,
                blocTunis,
                "CARDIO",
                "Cardiologie",
                "Consultations et soins cardiologiques"
        );

        findOrCreateService(
                serviceRepository,
                cliniqueSousse,
                blocSousse,
                "URGENCES",
                "Urgences",
                "Prise en charge des urgences médicales"
        );

        createAffectationIfMissing(
                affectationRepository,
                praticien1Id,
                cliniqueTunis
        );
        createAffectationIfMissing(
                affectationRepository,
                praticien2Id,
                cliniqueSousse
        );
    }

    private Clinique findOrCreateClinique(
            CliniqueRepository repository,
            String nom,
            String adresse,
            String telephone,
            String email,
            String ville
    ) {
        return repository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    Clinique clinique = new Clinique();
                    clinique.setNom(nom);
                    clinique.setAdresse(adresse);
                    clinique.setTelephone(telephone);
                    clinique.setEmail(email);
                    clinique.setVille(ville);
                    return repository.save(clinique);
                });
    }

    private Bloc findOrCreateBloc(
            BlocRepository repository,
            Clinique clinique,
            String code,
            String nom,
            String description
    ) {
        return repository
                .findByCodeIgnoreCaseAndCliniqueId(code, clinique.getId())
                .orElseGet(() -> {
                    Bloc bloc = new Bloc();
                    bloc.setCode(code);
                    bloc.setNom(nom);
                    bloc.setDescription(description);
                    bloc.setClinique(clinique);
                    return repository.save(bloc);
                });
    }

    private ServiceMedicale findOrCreateService(
            ServiceMedicaleRepository repository,
            Clinique clinique,
            Bloc bloc,
            String code,
            String libelle,
            String description
    ) {
        return repository
                .findByCodeIgnoreCaseAndCliniqueId(code, clinique.getId())
                .orElseGet(() -> {
                    ServiceMedicale service = new ServiceMedicale();
                    service.setCode(code);
                    service.setLibelle(libelle);
                    service.setDescription(description);
                    service.setActif(true);
                    service.setClinique(clinique);
                    service.setBloc(bloc);
                    return repository.save(service);
                });
    }

    private void createAffectationIfMissing(
            AffectationPraticienRepository repository,
            Long praticienId,
            Clinique clinique
    ) {
        boolean exists = repository
                .existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalse(
                        praticienId,
                        clinique.getId()
                );

        if (!exists) {
            AffectationPraticien affectation = new AffectationPraticien();
            affectation.setPraticienId(praticienId);
            affectation.setClinique(clinique);
            affectation.setActif(true);
            affectation.setDeleted(false);
            affectation.setDateAffectation(LocalDateTime.now());
            repository.save(affectation);
        }
    }
}
