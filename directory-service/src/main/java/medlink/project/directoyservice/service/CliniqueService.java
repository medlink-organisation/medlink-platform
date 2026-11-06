package medlink.project.directoyservice.service;

import medlink.project.directoyservice.dto.CliniqueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CliniqueService {
    CliniqueDto create(CliniqueDto dto);

    CliniqueDto getById(Long id);

    Page<CliniqueDto> getAll(Pageable pageable);

    CliniqueDto update(Long id, CliniqueDto dto);

    void delete(Long id);


    Page<CliniqueDto> search(
            String keyword,
            Pageable pageable
    );

    Page<CliniqueDto> getByVille(
            String ville,
            Pageable pageable
    );

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);
}
