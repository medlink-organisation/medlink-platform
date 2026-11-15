
package medlink.project.praticienservice.service;

import medlink.project.praticienservice.dto.SpecialiteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecialiteService {

    SpecialiteDto create(SpecialiteDto dto);

    SpecialiteDto update(Long id, SpecialiteDto dto);

    SpecialiteDto getById(Long id);

    SpecialiteDto getByCode(String code);

    Page<SpecialiteDto> getAll(Pageable pageable);
    SpecialiteDto changeStatus(Long id, Boolean actif);
    void delete(Long id);
}
