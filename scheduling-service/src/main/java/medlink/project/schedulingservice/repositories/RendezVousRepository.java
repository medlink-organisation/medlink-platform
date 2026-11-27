package medlink.project.schedulingservice.repositories;

import medlink.project.schedulingservice.entities.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RendezVousRepository extends JpaRepository<RendezVous , UUID> {

    @EntityGraph(attributePaths = "creneau")
    Page<RendezVous> findAllByPatientId(Long patientId, Pageable pageable);

    Page<RendezVous> findAllByCreneauId(Long creneauId, Pageable pageable);

    @Query("""
            select r from RendezVous r
            join fetch r.creneau c
            where c.praticienId = :praticienId
              and c.dateDebut < :fin
              and c.dateFin > :debut
            order by c.dateDebut asc
            """)
    List<RendezVous> findAgendaByPraticien(
            @Param("praticienId") Long praticienId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
            select r from RendezVous r
            join fetch r.creneau c
            where c.praticienId = :praticienId
              and r.status in :statuses
              and c.dateDebut between :debut and :fin
            order by c.dateDebut asc
            """)
    List<RendezVous> findDueByPraticien(
            @Param("praticienId") Long praticienId,
            @Param("statuses") Collection<medlink.project.schedulingservice.entities.StatusRendezVous> statuses,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin
    );

    @Query(
            value = """
                    select r from RendezVous r
                    join fetch r.creneau c
                    where c.praticienId = :praticienId
                      and r.status = :status
                      and c.dateDebut = (
                        select max(c2.dateDebut) from RendezVous r2
                        join r2.creneau c2
                        where c2.praticienId = :praticienId
                          and r2.patientId = r.patientId
                          and r2.status = :status
                      )
                    order by c.dateDebut desc
                    """,
            countQuery = """
                    select count(distinct r.patientId) from RendezVous r
                    join r.creneau c
                    where c.praticienId = :praticienId
                      and r.status = :status
                    """
    )
    Page<RendezVous> findLatestCompletedByPraticien(
            @Param("praticienId") Long praticienId,
            @Param("status") medlink.project.schedulingservice.entities.StatusRendezVous status,
            Pageable pageable
    );

    boolean existsByCreneauId(Long creneauId);


    boolean existsByPatientIdAndCreneauId(
            Long patientId,
            Long creneauId
    );

    boolean existsByPatientIdAndCreneauIdAndIdNot(
            Long patientId,
            Long creneauId,
            UUID id
    );

    boolean existsByIdAndPatientId(
            UUID id,
            Long patientId
    );
}
