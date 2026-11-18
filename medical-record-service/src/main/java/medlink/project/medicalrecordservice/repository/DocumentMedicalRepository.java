package medlink.project.medicalrecordservice.repository;

import medlink.project.medicalrecordservice.entities.DocumentMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, Long> {

    Page<DocumentMedical> findByDossierMedicalId(Long dossierId, Pageable pageable);

    boolean existsByObjectKey(String objectKey);

    Optional<DocumentMedical> findByObjectKey(String objectKey);
}
