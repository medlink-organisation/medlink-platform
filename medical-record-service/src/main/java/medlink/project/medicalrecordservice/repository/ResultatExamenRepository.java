package medlink.project.medicalrecordservice.repository;

import medlink.project.medicalrecordservice.entities.ResultatExamen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultatExamenRepository extends JpaRepository<ResultatExamen, UUID> {

    Page<ResultatExamen> findByDossierMedicalId(Long dossierId, Pageable pageable);
}
