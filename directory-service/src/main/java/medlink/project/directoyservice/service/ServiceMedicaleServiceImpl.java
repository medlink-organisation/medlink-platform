package medlink.project.directoyservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.ServiceMedicaleDto;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.entity.ServiceMedicale;
import medlink.project.directoyservice.mapper.ServiceMedicaleMapper;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.repositories.ServiceMedicaleRepository;
import medlink.project.directoyservice.validation.GenericEntityFinder;
import medlink.project.directoyservice.validation.ServiceMedicaleValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceMedicaleServiceImpl
        implements ServiceMedicaleService {

    private final ServiceMedicaleRepository serviceRepository;
    private final CliniqueRepository cliniqueRepository;
    private final BlocRepository blocRepository;

    private final ServiceMedicaleMapper serviceMapper;
    private final ServiceMedicaleValidator serviceValidator;
    private final GenericEntityFinder entityFinder;

    @Override
    public ServiceMedicaleDto create(
            ServiceMedicaleDto dto
    ) {

        serviceValidator.validateCreate(dto);

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        ServiceMedicale service =
                serviceMapper.toEntity(dto);

        service.setClinique(clinique);

        if (dto.getBlocId() != null) {

            Bloc bloc =
                    entityFinder.findByIdOrThrow(
                            blocRepository,
                            dto.getBlocId(),
                            "Bloc"
                    );

            service.setBloc(bloc);
        }

        return serviceMapper.toDto(
                serviceRepository.save(service)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceMedicaleDto getById(Long id) {

        ServiceMedicale service =
                entityFinder.findByIdOrThrow(
                        serviceRepository,
                        id,
                        "Service médical"
                );

        return serviceMapper.toDto(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> getAll(
            Pageable pageable
    ) {

        return serviceRepository
                .findAll(pageable)
                .map(serviceMapper::toDto);
    }

    @Override
    public ServiceMedicaleDto update(
            Long id,
            ServiceMedicaleDto dto
    ) {

        ServiceMedicale service =
                entityFinder.findByIdOrThrow(
                        serviceRepository,
                        id,
                        "Service médical"
                );

        serviceValidator.validateUpdate(
                id,
                dto
        );

        serviceMapper.updateEntityFromDto(
                dto,
                service
        );

        if (dto.getCliniqueId() != null) {

            Clinique clinique =
                    entityFinder.findByIdOrThrow(
                            cliniqueRepository,
                            dto.getCliniqueId(),
                            "Clinique"
                    );

            service.setClinique(clinique);
        }

        if (dto.getBlocId() != null) {

            Bloc bloc =
                    entityFinder.findByIdOrThrow(
                            blocRepository,
                            dto.getBlocId(),
                            "Bloc"
                    );

            service.setBloc(bloc);
        }

        return serviceMapper.toDto(
                serviceRepository.save(service)
        );
    }

    @Override
    public void delete(Long id) {

        ServiceMedicale service =
                entityFinder.findByIdOrThrow(
                        serviceRepository,
                        id,
                        "Service médical"
                );

        serviceRepository.delete(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> getByClinique(
            Long cliniqueId,
            Pageable pageable
    ) {

        return serviceRepository
                .findByCliniqueId(
                        cliniqueId,
                        pageable
                )
                .map(serviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> getByBloc(
            Long blocId,
            Pageable pageable
    ) {

        return serviceRepository
                .findByBlocIdBloc(
                        blocId,
                        pageable
                )
                .map(serviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> getActive(
            Pageable pageable
    ) {

        return serviceRepository
                .findByActifTrue(pageable)
                .map(serviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> getActiveByClinique(
            Long cliniqueId,
            Pageable pageable
    ) {

        return serviceRepository
                .findByCliniqueIdAndActifTrue(
                        cliniqueId,
                        pageable
                )
                .map(serviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceMedicaleDto> search(
            String keyword,
            Pageable pageable
    ) {

        return serviceRepository
                .findByLibelleContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                )
                .map(serviceMapper::toDto);
    }

    @Override
    public ServiceMedicaleDto activate(Long id) {

        return changeStatus(id, true);
    }

    @Override
    public ServiceMedicaleDto deactivate(Long id) {

        return changeStatus(id, false);
    }

    private ServiceMedicaleDto changeStatus(
            Long id,
            boolean actif
    ) {

        ServiceMedicale service =
                entityFinder.findByIdOrThrow(
                        serviceRepository,
                        id,
                        "Service médical"
                );

        service.setActif(actif);

        return serviceMapper.toDto(
                serviceRepository.save(service)
        );
    }
}