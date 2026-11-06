package medlink.project.directoyservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.CliniqueDto;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.mapper.CliniqueMapper;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.validation.CliniqueValidator;
import medlink.project.directoyservice.validation.GenericEntityFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CliniqueServiceImpl implements CliniqueService {

    private final CliniqueRepository cliniqueRepository;
    private final CliniqueMapper cliniqueMapper;

    private final CliniqueValidator cliniqueValidator;
    private final GenericEntityFinder entityFinder;


    @Override
    public CliniqueDto create(CliniqueDto dto) {

        cliniqueValidator.validateCreate(dto);

        Clinique clinique =
                cliniqueMapper.toEntity(dto);

        Clinique savedClinique =
                cliniqueRepository.save(clinique);

        return cliniqueMapper.toDto(savedClinique);
    }


    @Override
    @Transactional(readOnly = true)
    public CliniqueDto getById(Long id) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        id,
                        "Clinique"
                );

        return cliniqueMapper.toDto(clinique);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CliniqueDto> getAll(
            Pageable pageable
    ) {

        return cliniqueRepository
                .findAll(pageable)
                .map(cliniqueMapper::toDto);
    }


    @Override
    public CliniqueDto update(
            Long id,
            CliniqueDto dto
    ) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        id,
                        "Clinique"
                );

        cliniqueValidator.validateUpdate(
                id,
                dto
        );

        cliniqueMapper.updateEntityFromDto(
                dto,
                clinique
        );

        Clinique updatedClinique =
                cliniqueRepository.save(clinique);

        return cliniqueMapper.toDto(
                updatedClinique
        );
    }


    @Override
    public void delete(Long id) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        id,
                        "Clinique"
                );

        cliniqueRepository.delete(clinique);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CliniqueDto> search(
            String keyword,
            Pageable pageable
    ) {

        return cliniqueRepository
                .findByNomContainingIgnoreCaseOrVilleContainingIgnoreCaseOrAdresseContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                )
                .map(cliniqueMapper::toDto);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<CliniqueDto> getByVille(
            String ville,
            Pageable pageable
    ) {

        return cliniqueRepository
                .findByVilleIgnoreCase(
                        ville,
                        pageable
                )
                .map(cliniqueMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {

        return cliniqueRepository
                .existsByEmailIgnoreCase(email);
    }
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTelephone(
            String telephone
    ) {

        return cliniqueRepository
                .existsByTelephone(telephone);
    }



}