package medlink.project.praticienservice.service;

import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.dto.BookingPraticienDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface PraticienService {

    PraticienDto create(PraticienDto dto);

    PraticienDto update(Long id, PraticienDto dto);

    PraticienDto getById(Long id);

    List<PraticienDto> getByIds(Collection<Long> ids);

    PraticienDto getByKeycloakId(String keycloakId);

    PraticienDto getByEmail(String email);

    Page<PraticienDto> getAll(Pageable pageable);

    Page<PraticienDto> getBySpecialite(
            Long specialiteId,
            Pageable pageable
    );
    Page<BookingPraticienDto> getActiveForBooking(Long specialiteId, Pageable pageable);
    PraticienDto changeStatus(Long id, Boolean actif);
    void delete(Long id);
}
