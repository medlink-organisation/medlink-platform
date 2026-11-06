package medlink.project.directoyservice.service;

import medlink.project.directoyservice.dto.AffectationPraticienDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AffectationPraticienService {

    AffectationPraticienDto create(
            AffectationPraticienDto dto
    );

    AffectationPraticienDto getById(
            Long id
    );

    Page<AffectationPraticienDto> getAll(
            Pageable pageable
    );

    AffectationPraticienDto update(
            Long id,
            AffectationPraticienDto dto
    );

    Page<AffectationPraticienDto> getByPraticien(
            Long praticienId,
            Pageable pageable
    );

    Page<AffectationPraticienDto> getActiveByPraticien(
            Long praticienId,
            Pageable pageable
    );

    Page<AffectationPraticienDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    );

    Page<AffectationPraticienDto> getActiveByClinique(
            Long cliniqueId,
            Pageable pageable
    );

    AffectationPraticienDto desaffecter(
            Long id
    );

    AffectationPraticienDto reactiver(
            Long id
    );

    void delete(
            Long id
    );
}