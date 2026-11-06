package medlink.project.directoyservice.repositories;

import medlink.project.directoyservice.entity.Bloc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlocRepository extends JpaRepository<Bloc, Long> {
    Page<Bloc> findByCliniqueId(
            Long cliniqueId,
            Pageable pageable
    );

    Page<Bloc> findByNomContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String nom,
            String code,
            Pageable pageable
    );

    boolean existsByCodeIgnoreCaseAndCliniqueId(
            String code,
            Long cliniqueId
    );

    boolean existsByCodeIgnoreCaseAndCliniqueIdAndIdBlocNot(
            String code,
            Long cliniqueId,
            Long id
    );
}
