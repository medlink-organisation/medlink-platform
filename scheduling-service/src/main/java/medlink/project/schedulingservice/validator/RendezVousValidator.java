package medlink.project.schedulingservice.validator;

import lombok.RequiredArgsConstructor;
import medlink.project.schedulingservice.entities.Creneau;
import medlink.project.schedulingservice.entities.RendezVous;
import medlink.project.schedulingservice.entities.StatusRendezVous;
import medlink.project.schedulingservice.entities.StatutCreneau;
import medlink.project.schedulingservice.exception.BusinessException;
import medlink.project.schedulingservice.exception.RessourceAlreadyExistException;
import medlink.project.schedulingservice.exception.RessourceNotExistException;
import medlink.project.schedulingservice.repositories.CreneauRepository;
import medlink.project.schedulingservice.repositories.RendezVousRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RendezVousValidator {

    private static final List<StatusRendezVous> ACTIVE_STATUSES = List.of(
            StatusRendezVous.EN_ATTENTE,
            StatusRendezVous.CONFIRME
    );

    private final RendezVousRepository rendezVousRepository;
    private final CreneauRepository creneauRepository;

    public Creneau validateCreate(Long patientId, Long creneauId) {
        rendezVousRepository.lockPatientReservations(patientId);
        Creneau creneau = validateCreneauDisponible(creneauId);
        validateBookingRules(patientId, creneau, null);
        return creneau;
    }

    public RendezVous validateUpdate(UUID rendezVousId) {
        return validateExists(rendezVousId);
    }

    public RendezVous validateExists(UUID id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RessourceNotExistException(
                        "Le rendez-vous recherché n'existe pas."
                ));
    }

    public RendezVous validateDelete(UUID id) {
        return validateExists(id);
    }

    public RendezVous validateConfirmation(UUID id) {
        RendezVous rendezVous = validateExists(id);
        if (rendezVous.getStatus() != StatusRendezVous.EN_ATTENTE) {
            throw invalidTransition(rendezVous, StatusRendezVous.CONFIRME);
        }
        return rendezVous;
    }

    public RendezVous validateActive(UUID id) {
        RendezVous snapshot = validateExists(id);
        rendezVousRepository.lockPatientReservations(snapshot.getPatientId());
        RendezVous rendezVous = findForUpdate(id);
        if (!isActive(rendezVous)) {
            throw new BusinessException("Le rendez-vous est déjà clôturé.");
        }
        return rendezVous;
    }

    public RendezVous validatePatientAction(UUID id, Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new BusinessException("L'identité du patient est invalide.");
        }
        RendezVous snapshot = validateExists(id);
        if (!patientId.equals(snapshot.getPatientId())) {
            throw new RessourceNotExistException(
                    "Le rendez-vous recherché n'existe pas."
            );
        }
        rendezVousRepository.lockPatientReservations(patientId);
        RendezVous rendezVous = findForUpdate(id);
        if (!patientId.equals(rendezVous.getPatientId())) {
            throw new RessourceNotExistException(
                    "Le rendez-vous recherché n'existe pas."
            );
        }
        validateModifiable(rendezVous);
        return rendezVous;
    }

    public Creneau validateReport(RendezVous rendezVous, Long nouveauCreneauId) {
        Creneau nouveauCreneau = validateCreneauDisponible(nouveauCreneauId);
        Creneau ancienCreneau = rendezVous.getCreneau();
        if (!ancienCreneau.getPraticienId().equals(nouveauCreneau.getPraticienId())
                || !ancienCreneau.getCliniqueId().equals(nouveauCreneau.getCliniqueId())) {
            throw new BusinessException(
                    "Le nouveau créneau doit appartenir au même praticien et à la même clinique."
            );
        }
        validateBookingRules(rendezVous.getPatientId(), nouveauCreneau, rendezVous.getId());
        return nouveauCreneau;
    }

    public Creneau validateCreneauDisponible(Long id) {
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

    public boolean isActive(RendezVous rendezVous) {
        return rendezVous.getStatus() == StatusRendezVous.EN_ATTENTE
                || rendezVous.getStatus() == StatusRendezVous.CONFIRME;
    }

    private void validateModifiable(RendezVous rendezVous) {
        if (!isActive(rendezVous)) {
            throw new BusinessException("Le rendez-vous est déjà clôturé.");
        }
        if (!rendezVous.getCreneau().getDateDebut().isAfter(LocalDateTime.now())) {
            throw new BusinessException(
                    "Un rendez-vous déjà commencé ne peut plus être modifié ou annulé."
            );
        }
    }

    private void validateBookingRules(
            Long patientId,
            Creneau creneau,
            UUID excludedId
    ) {
        if (rendezVousRepository.existsActiveWithPraticien(
                patientId,
                creneau.getPraticienId(),
                ACTIVE_STATUSES,
                excludedId
        )) {
            throw new RessourceAlreadyExistException(
                    "Vous avez déjà un rendez-vous actif avec ce praticien. "
                            + "Vous pouvez le modifier ou l'annuler depuis Mes rendez-vous."
            );
        }

        if (rendezVousRepository.existsActiveOverlapping(
                patientId,
                creneau.getDateDebut(),
                creneau.getDateFin(),
                ACTIVE_STATUSES,
                excludedId
        )) {
            throw new RessourceAlreadyExistException(
                    "Vous avez déjà un rendez-vous prévu pendant cette période."
            );
        }
    }

    private RendezVous findForUpdate(UUID id) {
        return rendezVousRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RessourceNotExistException(
                        "Le rendez-vous recherché n'existe pas."
                ));
    }

    private BusinessException invalidTransition(
            RendezVous rendezVous,
            StatusRendezVous target
    ) {
        return new BusinessException(
                "Transition de " + rendezVous.getStatus()
                        + " vers " + target + " non autorisée."
        );
    }
}
