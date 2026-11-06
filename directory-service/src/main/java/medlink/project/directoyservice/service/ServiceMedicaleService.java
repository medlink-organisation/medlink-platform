package medlink.project.directoyservice.service;

import medlink.project.directoyservice.dto.ServiceMedicaleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceMedicaleService {

    ServiceMedicaleDto create(
            ServiceMedicaleDto dto
    );

    ServiceMedicaleDto getById(Long id);

    Page<ServiceMedicaleDto> getAll(
            Pageable pageable
    );

    ServiceMedicaleDto update(
            Long id,
            ServiceMedicaleDto dto
    );

    void delete(Long id);

    Page<ServiceMedicaleDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    );

    Page<ServiceMedicaleDto> getByBloc(
            Long blocId,
            Pageable pageable
    );

    Page<ServiceMedicaleDto> getActive(
            Pageable pageable
    );

    Page<ServiceMedicaleDto> getActiveByClinique(
            Long cliniqueId,
            Pageable pageable
    );

    Page<ServiceMedicaleDto> search(
            String keyword,
            Pageable pageable
    );

    ServiceMedicaleDto activate(Long id);

    ServiceMedicaleDto deactivate(Long id);
}