package medlink.project.directoyservice.validation;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.ServiceMedicaleDto;
import medlink.project.directoyservice.entity.Bloc;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import medlink.project.directoyservice.repositories.ServiceMedicaleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMedicaleValidator {

    private final ServiceMedicaleRepository serviceRepository;
    private final CliniqueRepository cliniqueRepository;
    private final BlocRepository blocRepository;

    private final GenericEntityFinder entityFinder;
    private final ValidationUtils validationUtils;


    public Clinique validateCreate(
            ServiceMedicaleDto dto
    ) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        validationUtils.throwIfExists(
                () -> serviceRepository
                        .existsByCodeIgnoreCaseAndCliniqueId(
                                dto.getCode(),
                                dto.getCliniqueId()
                        ),
                "Un service avec ce code existe déjà dans cette clinique"
        );

        validateBloc(dto.getBlocId(), clinique);

        return clinique;
    }


    public Clinique validateUpdate(
            Long serviceId,
            ServiceMedicaleDto dto
    ) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        if (dto.getCode() != null) {

            validationUtils.throwIfExists(
                    () -> serviceRepository
                            .existsByCodeIgnoreCaseAndCliniqueIdAndIdNot(
                                    dto.getCode(),
                                    dto.getCliniqueId(),
                                    serviceId
                            ),
                    "Un autre service avec ce code existe déjà dans cette clinique"
            );
        }

        validateBloc(dto.getBlocId(), clinique);

        return clinique;
    }


    public Bloc getBlocIfPresent(
            Long blocId
    ) {

        if (blocId == null) {
            return null;
        }

        return entityFinder.findByIdOrThrow(
                blocRepository,
                blocId,
                "Bloc"
        );
    }


    private void validateBloc(
            Long blocId,
            Clinique clinique
    ) {

        if (blocId == null) {
            return;
        }

        Bloc bloc =
                entityFinder.findByIdOrThrow(
                        blocRepository,
                        blocId,
                        "Bloc"
                );

        validationUtils.throwIf(
                !bloc.getClinique()
                        .getId()
                        .equals(clinique.getId()),

                "Le bloc n'appartient pas à cette clinique"
        );
    }
}