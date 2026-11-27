package medlink.project.praticienservice.repository;

import medlink.project.praticienservice.entity.Specialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialiteRepository
        extends JpaRepository<Specialite, Long> {


    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNomIgnoreCase(String nom);

    boolean existsByCodeIgnoreCaseAndIdNot(
            String code,
            Long id
    );

    boolean existsByNomIgnoreCaseAndIdNot(
            String nom,
            Long id
    );

    Optional<Specialite> findByCode(String code);

    Optional<Specialite> findByCodeIgnoreCase(String code);

    Optional<Specialite> findByNomIgnoreCase(String nom);

    Page<Specialite> findByActifTrue(Pageable pageable);



}
