package medlink.project.praticienservice.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.entity.Praticien;
import medlink.project.praticienservice.exception.BusinessException;
import medlink.project.praticienservice.exception.ResourceNotFoundException;
import medlink.project.praticienservice.repository.PraticienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PraticienValidator {


    private final PraticienRepository repository;

    // Vérifier si le praticien existe
    public Praticien validateExists(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Praticien introuvable avec ID : " + id
                        )
                );
    }

    // Vérifier si un compte Keycloak est déjà associé
    public void validateKeycloakIdNotAssigned(String keycloakId) {

        if (repository.existsByKeycloakId(keycloakId)) {
            throw new BusinessException(
                    "Ce compte Keycloak est déjà associé à un praticien"
            );
        }
    }

    // CREATE
    public void validateCreate(PraticienDto dto) {

        validateKeycloakIdNotAssigned(dto.getKeycloakId());

        if (repository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException(
                    "Cet email est déjà utilisé"
            );
        }

        if (dto.getNumeroOrdre() != null &&
                repository.existsByNumeroOrdre(dto.getNumeroOrdre())) {

            throw new BusinessException(
                    "Ce numéro d'ordre est déjà utilisé"
            );
        }
    }

    // UPDATE
    public Praticien validateUpdate(Long id, PraticienDto dto) {

        Praticien praticien = validateExists(id);

        if (dto.getKeycloakId() != null &&
                !dto.getKeycloakId().equals(praticien.getKeycloakId())) {

            throw new BusinessException(
                    "Modification du KeycloakId interdite"
            );
        }

        if (dto.getEmail() != null &&
                repository.existsByEmailIgnoreCaseAndIdNot(
                        dto.getEmail(), id)) {

            throw new BusinessException(
                    "Email déjà utilisé par un autre praticien"
            );
        }

        if (dto.getNumeroOrdre() != null &&
                repository.existsByNumeroOrdreAndIdNot(
                        dto.getNumeroOrdre(), id)) {

            throw new BusinessException(
                    "Numéro d'ordre déjà utilisé par un autre praticien"
            );
        }

        return praticien;
    }

    // DELETE
    public Praticien validateDelete(Long id) {

        return validateExists(id);
    }
}
