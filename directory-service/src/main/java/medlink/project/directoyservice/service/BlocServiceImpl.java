package medlink.project.directoyservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.BlocDto;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.mapper.BlocMapper;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.validation.BlocValidator;
import medlink.project.directoyservice.validation.GenericEntityFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlocServiceImpl
        implements BlocService {

    private final BlocRepository blocRepository;
    private final CliniqueRepository cliniqueRepository;

    private final BlocMapper blocMapper;
    private final BlocValidator blocValidator;
    private final GenericEntityFinder entityFinder;

    @Override
    public BlocDto create(BlocDto dto) {

        Clinique clinique =
                blocValidator.validateCreate(dto);

        Bloc bloc =
                blocMapper.toEntity(dto);

        bloc.setClinique(clinique);

        return blocMapper.toDto(
                blocRepository.save(bloc)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BlocDto getById(Long id) {

        Bloc bloc =
                entityFinder.findByIdOrThrow(
                        blocRepository,
                        id,
                        "Bloc"
                );

        return blocMapper.toDto(bloc);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlocDto> getAll(
            Pageable pageable
    ) {

        return blocRepository
                .findAll(pageable)
                .map(blocMapper::toDto);
    }

    @Override
    public BlocDto update(
            Long id,
            BlocDto dto
    ) {

        Bloc bloc =
                entityFinder.findByIdOrThrow(
                        blocRepository,
                        id,
                        "Bloc"
                );

        blocValidator.validateUpdate(
                id,
                dto
        );

        blocMapper.updateEntityFromDto(
                dto,
                bloc
        );

        if (dto.getCliniqueId() != null) {

            Clinique clinique =
                    entityFinder.findByIdOrThrow(
                            cliniqueRepository,
                            dto.getCliniqueId(),
                            "Clinique"
                    );

            bloc.setClinique(clinique);
        }

        return blocMapper.toDto(
                blocRepository.save(bloc)
        );
    }

    @Override
    public void delete(Long id) {

        Bloc bloc =
                entityFinder.findByIdOrThrow(
                        blocRepository,
                        id,
                        "Bloc"
                );

        blocRepository.delete(bloc);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlocDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    ) {

        entityFinder.findByIdOrThrow(
                cliniqueRepository,
                cliniqueId,
                "Clinique"
        );

        return blocRepository
                .findByCliniqueId(
                        cliniqueId,
                        pageable
                )
                .map(blocMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlocDto> search(
            String keyword,
            Pageable pageable
    ) {

        return blocRepository
                .findByNomContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                )
                .map(blocMapper::toDto);
    }
}