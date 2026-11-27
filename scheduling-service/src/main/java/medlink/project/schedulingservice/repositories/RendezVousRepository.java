package medlink.project.schedulingservice.repositories;

import medlink.project.schedulingservice.entities.RendezVous;
import medlink.project.schedulingservice.entities.StatusRendezVous;
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

    @Override
    @EntityGraph(attributePaths = "creneau")
    Page<RendezVous> findAll(Pageable pageable);

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
                  and r.status in :statuses
                  and not exists (
                      select r2.id from RendezVous r2
                      join r2.creneau c2
                      where c2.praticienId = :praticienId
                        and r2.patientId = r.patientId
                        and r2.status in :statuses
                        and (
                            (r2.status = :completed
                             and r.status <> :completed)
                            or (
                                r2.status = r.status
                                and (
                                    c2.dateDebut > c.dateDebut
                                    or (
                                        c2.dateDebut = c.dateDebut
                                        and r2.id > r.id
                                    )
                                )
                            )
                        )
                  )
                order by c.dateDebut desc, r.id desc
                """,
            countQuery = """
                select count(distinct r.patientId)
                from RendezVous r
                join r.creneau c
                where c.praticienId = :praticienId
                  and r.status in :statuses
                """
    )
    Page<RendezVous> findPatientsByPraticien(
            @Param("praticienId") Long praticienId,
            @Param("statuses") Collection<StatusRendezVous> statuses,
            @Param("completed") StatusRendezVous completed,
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
