package medlink.project.schedulingservice.repositories;

import medlink.project.schedulingservice.entities.Creneau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CreneauRepository extends JpaRepository<Creneau,Long> {


    Page<Creneau> findAllByPraticienId(Long partcienId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Creneau c where c.id = :id")
    Optional<Creneau> findByIdForUpdate(@Param("id") Long id);

    boolean existsByIdAndPraticienId(
            Long id,
            Long praticienId
    );

    boolean existsByPraticienIdAndCliniqueIdAndDateDebutAndDateFin(
            Long praticienId,
            Long cliniqueId,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    );

    boolean existsByPraticienIdAndCliniqueIdAndDateDebutAndDateFinAndIdNot(
            Long praticienId,
            Long cliniqueId,
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Long id
    );


}
