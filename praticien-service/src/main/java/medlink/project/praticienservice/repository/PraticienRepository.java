package medlink.project.praticienservice.repository;

import medlink.project.praticienservice.entity.Praticien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PraticienRepository
        extends JpaRepository<Praticien, Long> {

    Optional<Praticien> findByKeycloakId(String keycloakId);

    Optional<Praticien> findByEmail(String email);

    boolean existsByKeycloakId(String keycloakId);

    boolean existsByEmail(String email);

    boolean existsByNumeroOrdre(String numeroOrdre);

    Page<Praticien> findBySpecialiteId(
            Long specialiteId,
            Pageable pageable
    );

    Page<Praticien> findBySpecialiteIdAndActifTrue(
            Long specialiteId,
            Pageable pageable
    );

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Long id
    );

    boolean existsByKeycloakIdAndIdNot(
            String keycloakId,
            Long id
    );

    boolean existsByNumeroOrdreAndIdNot(
            String numeroOrdre,
            Long id
    );

    boolean existsBySpecialiteId(Long specialiteId);
}
