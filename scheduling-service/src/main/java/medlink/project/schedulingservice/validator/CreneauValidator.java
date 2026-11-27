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
        validateNoOverlap(dto.getPraticienId(), dto.getDateDebut(), dto.getDateFin(), null);
    }

    public Creneau validateUpdate(Long id, CreneauDto dto) {
        Creneau creneau = validateExists(id);

        if (dto.getPraticienId() != null
                && !dto.getPraticienId().equals(creneau.getPraticienId())) {
            throw new BusinessException("Le praticien d'un créneau ne peut pas être modifié.");
        }

        LocalDateTime dateDebut = dto.getDateDebut() == null
                ? creneau.getDateDebut()
                : dto.getDateDebut();
        LocalDateTime dateFin = dto.getDateFin() == null
                ? creneau.getDateFin()
                : dto.getDateFin();

        validateDates(dateDebut, dateFin);

        validateNoOverlap(creneau.getPraticienId(), dateDebut, dateFin, id);

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

    public Creneau validateDisponibleForUpdate(Long id) {
        Creneau creneau = creneauRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RessourceNotExistException(
                        "Le créneau recherché n'existe pas."
                ));
        if (creneau.getStatut() != StatutCreneau.DISPONIBLE
                || !creneau.getDateDebut().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Le créneau n'est pas disponible.");
        }
        return creneau;
    }

    private void validateDates(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            throw new BusinessException(
                    "La date de fin doit être postérieure à la date de début."
            );
        }
    }

    public boolean overlaps(
            Long praticienId,
            LocalDateTime debut,
            LocalDateTime fin
    ) {
        return creneauRepository.existsOverlapping(praticienId, debut, fin, null);
    }

    private void validateNoOverlap(
            Long praticienId,
            LocalDateTime debut,
            LocalDateTime fin,
            Long excludedId
    ) {
        if (creneauRepository.existsOverlapping(praticienId, debut, fin, excludedId)) {
            throw new RessourceAlreadyExistException(
                    "Le praticien possède déjà un créneau qui chevauche cette période."
            );
        }
    }
}
