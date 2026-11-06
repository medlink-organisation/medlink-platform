package medlink.project.directoyservice.repositories;

import medlink.project.directoyservice.entity.Clinique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CliniqueRepository extends JpaRepository<Clinique, Long> {
    boolean existsByAdresse(String adresse);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTelephone(String telephone);



    boolean existsByAdresseIgnoreCaseAndIdNot(
            String adresse,
            Long id
    );


    // =========================
    // FILTER
    // =========================

    Page<Clinique> findByVilleIgnoreCase(
            String ville,
            Pageable pageable
    );


    // =========================
    // SEARCH
    // =========================

    Page<Clinique>
    findByNomContainingIgnoreCaseOrVilleContainingIgnoreCaseOrAdresseContainingIgnoreCase(
            String nom,
            String ville,
            String adresse,
            Pageable pageable
    );

}
