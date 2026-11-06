package medlink.project.directoyservice.service;

import medlink.project.directoyservice.dto.BlocDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlocService {
    BlocDto create(BlocDto dto);

    BlocDto getById(Long id);

    Page<BlocDto> getAll(Pageable pageable);

    BlocDto update(
            Long id,
            BlocDto dto
    );

    void delete(Long id);

    Page<BlocDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    );

    Page<BlocDto> search(
            String keyword,
            Pageable pageable
    );
}
