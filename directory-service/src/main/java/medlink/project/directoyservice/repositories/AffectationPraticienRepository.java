package medlink.project.directoyservice.repositories;

import medlink.project.directoyservice.entity.AffectationPraticien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffectationPraticienRepository
        extends JpaRepository<AffectationPraticien, Long> {

    Page<AffectationPraticien> findByPraticienId(
            Long praticienId,
            Pageable pageable
    );

    Page<AffectationPraticien>
    findByPraticienIdAndActifTrueAndDeletedFalse(
            Long praticienId,
            Pageable pageable
    );

    Page<AffectationPraticien>
    findByClinique_Id(
            Long cliniqueId,
            Pageable pageable
    );

    Page<AffectationPraticien>
    findByClinique_IdAndActifTrueAndDeletedFalse(
            Long cliniqueId,
            Pageable pageable
    );

    Page<AffectationPraticien>
    findByDeletedFalse(
            Pageable pageable
    );

    boolean
    existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalse(
            Long praticienId,
            Long cliniqueId
    );

    boolean
    existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalseAndIdNot(
            Long praticienId,
            Long cliniqueId,
            Long id
    );
}
