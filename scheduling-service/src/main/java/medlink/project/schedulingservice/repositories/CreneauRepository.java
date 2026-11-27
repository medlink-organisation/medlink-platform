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
import java.util.List;
import java.util.Optional;

public interface CreneauRepository extends JpaRepository<Creneau,Long> {


    Page<Creneau> findAllByPraticienId(Long partcienId, Pageable pageable);

    @Query("""
            select c from Creneau c
            where (:praticienId is null or c.praticienId = :praticienId)
              and (:cliniqueId is null or c.cliniqueId = :cliniqueId)
              and c.dateDebut < :fin
              and c.dateFin > :debut
            order by c.dateDebut asc
            """)
    List<Creneau> findCalendar(
            @Param("praticienId") Long praticienId,
            @Param("cliniqueId") Long cliniqueId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin
    );

    @Query("select min(c.dateDebut) from Creneau c where c.praticienId = :praticienId")
    Optional<LocalDateTime> findActivityStart(@Param("praticienId") Long praticienId);

    @Query("select max(c.dateFin) from Creneau c where c.praticienId = :praticienId")
    Optional<LocalDateTime> findActivityEnd(@Param("praticienId") Long praticienId);

    @Query("""
            select (count(c) > 0) from Creneau c
            where c.praticienId = :praticienId
              and c.dateDebut < :fin
              and c.dateFin > :debut
              and (:excludedId is null or c.id <> :excludedId)
            """)
    boolean existsOverlapping(
            @Param("praticienId") Long praticienId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            @Param("excludedId") Long excludedId
    );

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
