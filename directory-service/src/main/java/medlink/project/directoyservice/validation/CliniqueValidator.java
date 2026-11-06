package medlink.project.directoyservice.validation;

import lombok.RequiredArgsConstructor;
import medlink.project.directoyservice.dto.CliniqueDto;
import medlink.project.directoyservice.repositories.CliniqueRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CliniqueValidator {

    private final CliniqueRepository cliniqueRepository;
    private final ValidationUtils validationUtils;

    public void validateCreate(CliniqueDto dto) {

        if (dto.getEmail() != null) {
            validationUtils.throwIfExists(
                    () -> cliniqueRepository
                            .existsByEmailIgnoreCase(dto.getEmail()),
                    "Une clinique avec cet email existe déjà"
            );
        }

        if (dto.getTelephone() != null) {
            validationUtils.throwIfExists(
                    () -> cliniqueRepository
                            .existsByTelephone(dto.getTelephone()),
                    "Une clinique avec ce téléphone existe déjà"
            );
        }
    }

    public void validateUpdate(
            Long cliniqueId,
            CliniqueDto dto
    ) {

        if (dto.getEmail() != null) {

            validationUtils.throwIfExists(
                    () -> cliniqueRepository
                            .existsByEmailIgnoreCase(
                                    dto.getEmail()

                            ),
                    "Cet email est déjà utilisé par une autre clinique"
            );
        }

        if (dto.getTelephone() != null) {

            validationUtils.throwIfExists(
                    () -> cliniqueRepository
                            .existsByTelephone(
                                    dto.getTelephone()

                            ),
                    "Ce téléphone est déjà utilisé par une autre clinique"
            );
        }
    }
}