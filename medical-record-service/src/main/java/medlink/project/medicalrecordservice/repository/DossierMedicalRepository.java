package medlink.project.medicalrecordservice.repository;

import medlink.project.medicalrecordservice.entities.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    Optional<DossierMedical> findByPatientId(Long patientId);

    boolean existsByPatientId(Long patientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from DossierMedical d where d.id = :id")
    Optional<DossierMedical> findByIdForUpdate(Long id);
}
