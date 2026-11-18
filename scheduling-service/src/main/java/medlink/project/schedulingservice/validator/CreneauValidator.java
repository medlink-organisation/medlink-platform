package medlink.project.schedulingservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.dto.CreneauDto;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.exception.RessourceAlreadyExistException;
import medlink.project.schedulingservice.exception.RessourceNotExistException;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreneauValidator {

    private final CreneauRepository creneauRepository;
    private final RendezVousRepository rendezVousRepository;

    public void validateCreate(CreneauDto dto) {
        validateDates(dto.getDateDebut(), dto.getDateFin());

        boolean exists = creneauRepository
                .existsByPraticienIdAndCliniqueIdAndDateDebutAndDateFin(
                        dto.getPraticienId(),
                        dto.getCliniqueId(),
                        dto.getDateDebut(),
                        dto.getDateFin()
                );

        if (exists) {
            throw new RessourceAlreadyExistException("Ce créneau existe déjà.");
        }
    }

    public Creneau validateUpdate(Long id, CreneauDto dto) {
        Creneau creneau = validateExists(id);

        if (dto.getPraticienId() != null
                && !dto.getPraticienId().equals(creneau.getPraticienId())) {
            throw new BusinessException("Le praticien d'un créneau ne peut pas être modifié.");
        }

        Long cliniqueId = dto.getCliniqueId() == null
                ? creneau.getCliniqueId()
                : dto.getCliniqueId();
        LocalDateTime dateDebut = dto.getDateDebut() == null
                ? creneau.getDateDebut()
                : dto.getDateDebut();
        LocalDateTime dateFin = dto.getDateFin() == null
                ? creneau.getDateFin()
                : dto.getDateFin();

        validateDates(dateDebut, dateFin);

        boolean duplicate = creneauRepository
                .existsByPraticienIdAndCliniqueIdAndDateDebutAndDateFinAndIdNot(
                        creneau.getPraticienId(),
                        cliniqueId,
                        dateDebut,
                        dateFin,
                        id
                );

        if (duplicate) {
            throw new RessourceAlreadyExistException("Un autre créneau identique existe déjà.");
        }

        return creneau;
    }

    public Creneau validateExists(Long id) {
        return creneauRepository.findById(id)
                .orElseThrow(() -> new RessourceNotExistException(
                        "Le créneau recherché n'existe pas."
                ));
    }

    public Creneau validateDelete(Long id) {
        Creneau creneau = validateExists(id);
        if (rendezVousRepository.existsByCreneauId(id)) {
            throw new BusinessException(
                    "Ce créneau possède un rendez-vous et ne peut pas être supprimé."
            );
        }
        return creneau;
    }

    public boolean isDisponible(Long id) {
        Creneau creneau = validateExists(id);
        return creneau.getStatut() == StatutCreneau.DISPONIBLE
                && creneau.getDateDebut().isAfter(LocalDateTime.now());
    }

    private void validateDates(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new BusinessException(
                    "La date de fin doit être postérieure à la date de début."
            );
        }
    }
}
