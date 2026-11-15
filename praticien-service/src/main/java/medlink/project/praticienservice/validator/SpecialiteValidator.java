package medlink.project.praticienservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.praticienservice.dto.SpecialiteDto;
import medlink.project.praticienservice.entity.Specialite;
import medlink.project.praticienservice.exception.BusinessException;
import medlink.project.praticienservice.exception.ResourceNotFoundException;
import medlink.project.praticienservice.repository.PraticienRepository;
import medlink.project.praticienservice.repository.SpecialiteRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class SpecialiteValidator {
    private final SpecialiteRepository repository;
    private final PraticienRepository praticienRepository;
    public Specialite validateExists(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Spécialité introuvable avec ID : " + id
                        )
                );
    }

    // CREATE
    public void validateCreate(SpecialiteDto dto) {

        if (repository.existsByCodeIgnoreCase(dto.getCode())) {
            throw new BusinessException(
                    "Ce code de spécialité existe déjà"
            );
        }

        if (repository.existsByNomIgnoreCase(dto.getNom())) {
            throw new BusinessException(
                    "Cette spécialité existe déjà"
            );
        }
    }

    // UPDATE
    public Specialite validateUpdate(Long id, SpecialiteDto dto) {

        Specialite specialite = validateExists(id);

        if (dto.getCode() != null &&
                repository.existsByCodeIgnoreCaseAndIdNot(
                        dto.getCode(), id)) {

            throw new BusinessException(
                    "Ce code appartient à une autre spécialité"
            );
        }

        if (dto.getNom() != null &&
                repository.existsByNomIgnoreCaseAndIdNot(
                        dto.getNom(), id)) {

            throw new BusinessException(
                    "Ce nom appartient à une autre spécialité"
            );
        }

        return specialite;
    }

    // DELETE
    public Specialite validateDelete(Long id) {

        Specialite specialite = validateExists(id);

        if (praticienRepository.existsBySpecialiteId(id)) {
            throw new BusinessException(
                    "Impossible de supprimer cette spécialité : " +
                            "elle est associée à des praticiens"
            );
        }

        return specialite;
    }
}
