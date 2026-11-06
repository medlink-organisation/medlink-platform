package medlink.project.directoyservice.validation;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.AffectationPraticienDto;
import medlink.project.directoyservice.entity.AffectationPraticien;
import medlink.project.directoyservice.entity.Clinique;
import medlink.project.directoyservice.repositories.AffectationPraticienRepository;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AffectationPraticienValidator {

    private final AffectationPraticienRepository affectationRepository;
    private final CliniqueRepository cliniqueRepository;

    private final GenericEntityFinder entityFinder;
    private final ValidationUtils validationUtils;


    // ==============================
    // CREATE
    // ==============================

    public Clinique validateCreate(
            AffectationPraticienDto dto
    ) {

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        dto.getCliniqueId(),
                        "Clinique"
                );

        validationUtils.throwIfExists(
                () -> affectationRepository
                        .existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalse(
                                dto.getPraticienId(),
                                dto.getCliniqueId()
                        ),
                "Le praticien est déjà affecté à cette clinique"
        );

        return clinique;
    }


    // ==============================
    // UPDATE
    // ==============================

    public Clinique validateUpdate(
            Long affectationId,
            AffectationPraticienDto dto,
            AffectationPraticien current
    ) {

        Long cliniqueId =
                dto.getCliniqueId() != null
                        ? dto.getCliniqueId()
                        : current.getClinique().getId();

        Long praticienId =
                dto.getPraticienId() != null
                        ? dto.getPraticienId()
                        : current.getPraticienId();

        Clinique clinique =
                entityFinder.findByIdOrThrow(
                        cliniqueRepository,
                        cliniqueId,
                        "Clinique"
                );

        validationUtils.throwIfExists(
                () -> affectationRepository
                        .existsByPraticienIdAndClinique_IdAndActifTrueAndDeletedFalseAndIdNot(
                                praticienId,
                                cliniqueId,
                                affectationId
                        ),
                "Une autre affectation identique existe déjà"
        );

        return clinique;
    }
}