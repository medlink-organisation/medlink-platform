package medlink.project.medicalrecordservice.repository;

import medlink.project.medicalrecordservice.entities.CompteRendu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface CompteRenduRepository extends JpaRepository<CompteRendu, UUID> {

    Page<CompteRendu> findByDossierMedicalId(Long dossierId, Pageable pageable);

    Page<CompteRendu> findByPraticienId(UUID praticienId, Pageable pageable);

    boolean existsByRendezVousId(UUID rendezVousId);

    Optional<CompteRendu> findByRendezVousId(UUID rendezVousId);
}
