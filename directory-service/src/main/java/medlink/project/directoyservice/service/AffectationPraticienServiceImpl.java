package medlink.project.directoyservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.AffectationPraticienDto;
import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.mapper.AffectationPraticienMapper;
import medlink.project.directoyservice.repositories.AffectationPraticienRepository;
import medlink.project.directoyservice.validation.AffectationPraticienValidator;
import medlink.project.directoyservice.validation.GenericEntityFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AffectationPraticienServiceImpl
        implements AffectationPraticienService {

    private final AffectationPraticienRepository affectationRepository;

    private final AffectationPraticienMapper affectationMapper;

    private final AffectationPraticienValidator affectationValidator;

    private final GenericEntityFinder entityFinder;


    @Override
    public AffectationPraticienDto create(
            AffectationPraticienDto dto
    ) {

        Clinique clinique =
                affectationValidator.validateCreate(dto);

        AffectationPraticien affectation =
                affectationMapper.toEntity(
                        dto,
                        clinique
                );

        AffectationPraticien saved =
                affectationRepository.save(
                        affectation
                );

        return affectationMapper.toDto(saved);
    }



    @Override
    @Transactional(readOnly = true)
    public AffectationPraticienDto getById(
            Long id
    ) {

        AffectationPraticien affectation =
                entityFinder.findByIdOrThrow(
                        affectationRepository,
                        id,
                        "Affectation praticien"
                );

        return affectationMapper.toDto(
                affectation
        );
    }



    @Override
    @Transactional(readOnly = true)
    public Page<AffectationPraticienDto> getAll(
            Pageable pageable
    ) {

        return affectationRepository
                .findByDeletedFalse(pageable)
                .map(affectationMapper::toDto);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public AffectationPraticienDto update(
            Long id,
            AffectationPraticienDto dto
    ) {

        AffectationPraticien affectation =
                entityFinder.findByIdOrThrow(
                        affectationRepository,
                        id,
                        "Affectation praticien"
                );

        Clinique clinique =
                affectationValidator.validateUpdate(
                        id,
                        dto,
                        affectation
                );

        affectationMapper.updateEntity(
                dto,
                clinique,
                affectation
        );

        AffectationPraticien updated =
                affectationRepository.save(
                        affectation
                );

        return affectationMapper.toDto(
                updated
        );
    }


    // =========================================================
    // GET BY PRATICIEN
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<AffectationPraticienDto> getByPraticien(
            Long praticienId,
            Pageable pageable
    ) {

        return affectationRepository
                .findByPraticienId(
                        praticienId,
                        pageable
                )
                .map(affectationMapper::toDto);
    }


    // =========================================================
    // ACTIVE BY PRATICIEN
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<AffectationPraticienDto> getActiveByPraticien(
            Long praticienId,
            Pageable pageable
    ) {

        return affectationRepository
                .findByPraticienIdAndActifTrueAndDeletedFalse(
                        praticienId,
                        pageable
                )
                .map(affectationMapper::toDto);
    }


    // =========================================================
    // GET BY CLINIQUE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<AffectationPraticienDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    ) {

        return affectationRepository
                .findByClinique_Id(
                        cliniqueId,
                        pageable
                )
                .map(affectationMapper::toDto);
    }


    // =========================================================
    // ACTIVE BY CLINIQUE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public Page<AffectationPraticienDto> getActiveByClinique(
            Long cliniqueId,
            Pageable pageable
    ) {

        return affectationRepository
                .findByClinique_IdAndActifTrueAndDeletedFalse(
                        cliniqueId,
                        pageable
                )
                .map(affectationMapper::toDto);
    }


    // =========================================================
    // DESAFFECTER
    // =========================================================

    @Override
    public AffectationPraticienDto desaffecter(
            Long id
    ) {

        AffectationPraticien affectation =
                entityFinder.findByIdOrThrow(
                        affectationRepository,
                        id,
                        "Affectation praticien"
                );

        affectation.desaffecter(

        );

        AffectationPraticien updated =
                affectationRepository.save(
                        affectation
                );

        return affectationMapper.toDto(
                updated
        );
    }


    // =========================================================
    // REACTIVER
    // =========================================================

    @Override
    public AffectationPraticienDto reactiver(
            Long id
    ) {

        AffectationPraticien affectation =
                entityFinder.findByIdOrThrow(
                        affectationRepository,
                        id,
                        "Affectation praticien"
                );

        affectation.reactiver(

        );

        AffectationPraticien updated =
                affectationRepository.save(
                        affectation
                );

        return affectationMapper.toDto(
                updated
        );
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    public void delete(
            Long id
    ) {

        AffectationPraticien affectation =
                entityFinder.findByIdOrThrow(
                        affectationRepository,
                        id,
                        "Affectation praticien"
                );

        affectation.softDelete(
                
        );

        affectationRepository.save(
                affectation
        );
    }
}