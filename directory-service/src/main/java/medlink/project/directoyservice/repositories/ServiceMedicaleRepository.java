package medlink.project.directoyservice.repositories;

import medlink.project.directoyservice.entity.ServiceMedicale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceMedicaleRepository
        extends JpaRepository<ServiceMedicale, Long> {

    Page<ServiceMedicale> findByCliniqueId(
            Long cliniqueId,
            Pageable pageable
    );

    Page<ServiceMedicale> findByBlocIdBloc(
            Long blocId,
            Pageable pageable
    );

    Page<ServiceMedicale> findByActifTrue(
            Pageable pageable
    );

    Page<ServiceMedicale>
    findByCliniqueIdAndActifTrue(
            Long cliniqueId,
            Pageable pageable
    );

    Page<ServiceMedicale>
    findByLibelleContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String libelle,
            String code,
            Pageable pageable
    );

    boolean existsByCodeIgnoreCaseAndCliniqueId(
            String code,
            Long cliniqueId
    );

    boolean existsByCodeIgnoreCaseAndCliniqueIdAndIdNot(
            String code,
            Long cliniqueId,
            Long id
    );
}