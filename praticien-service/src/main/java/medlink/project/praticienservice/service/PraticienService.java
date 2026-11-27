package medlink.project.praticienservice.service;

import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.dto.BookingPraticienDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PraticienService {

    PraticienDto create(PraticienDto dto);

    PraticienDto update(Long id, PraticienDto dto);

    PraticienDto getById(Long id);

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
