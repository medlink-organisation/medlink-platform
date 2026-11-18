package medlink.project.schedulingservice.repositories;

import medlink.project.schedulingservice.entities.RendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RendezVousRepository extends JpaRepository<RendezVous , UUID> {

    Page<RendezVous> findAllByPatientId(Long patientId, Pageable pageable);

    Page<RendezVous> findAllByCreneauId(Long creneauId, Pageable pageable);

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
