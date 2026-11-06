package medlink.project.directoyservice.validation;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.BlocDto;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.repositories.BlocRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlocValidator {

    private final BlocRepository blocRepository;
    private final CliniqueRepository cliniqueRepository;

    private final GenericEntityFinder entityFinder;
    private final ValidationUtils validationUtils;


    public Clinique validateCreate(BlocDto dto) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        validationUtils.throwIfExists(
                () -> blocRepository
                        .existsByCodeIgnoreCaseAndCliniqueId(
                                dto.getCode(),
                                dto.getCliniqueId()
                        ),
                "Un bloc avec ce code existe déjà dans cette clinique"
        );

        return clinique;
    }


    public Clinique validateUpdate(
            Long blocId,
            BlocDto dto
    ) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        if (dto.getCode() != null) {

            validationUtils.throwIfExists(
                    () -> blocRepository
                            .existsByCodeIgnoreCaseAndCliniqueIdAndIdBlocNot(
                                    dto.getCode(),
                                    dto.getCliniqueId(),
                                    blocId
                            ),
                    "Un autre bloc avec ce code existe déjà dans cette clinique"
            );
        }

        return clinique;
    }
}