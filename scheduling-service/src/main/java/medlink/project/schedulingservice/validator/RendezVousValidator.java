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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RendezVousValidator {

    private final RendezVousRepository rendezVousRepository;
    private final CreneauRepository creneauRepository;

    public Creneau validateCreate(Long patientId, Long creneauId) {
        Creneau creneau = validateCreneauDisponible(creneauId);
        validateNoDuplicate(patientId, creneauId, null);
        return creneau;
    }

    public RendezVous validateUpdate(UUID rendezVousId) {
        RendezVous rendezVous = validateExists(rendezVousId);
        validateNoDuplicate(
                rendezVous.getPatientId(),
                rendezVous.getCreneau().getId(),
                rendezVousId
        );
        return rendezVous;
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
        RendezVous rendezVous = validateExists(id);
        validateActive(rendezVous);
        return rendezVous;
    }

    public Creneau validateReport(RendezVous rendezVous, Long nouveauCreneauId) {
        Creneau nouveauCreneau = validateCreneauDisponible(nouveauCreneauId);
        validateNoDuplicate(rendezVous.getPatientId(), nouveauCreneauId, rendezVous.getId());
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

    private void validateActive(RendezVous rendezVous) {
        if (!isActive(rendezVous)) {
            throw new BusinessException("Le rendez-vous est déjà clôturé.");
        }
    }

    private void validateNoDuplicate(Long patientId, Long creneauId, UUID excludedId) {
        boolean duplicate = excludedId == null
                ? rendezVousRepository.existsByPatientIdAndCreneauId(patientId, creneauId)
                : rendezVousRepository.existsByPatientIdAndCreneauIdAndIdNot(
                        patientId,
                        creneauId,
                        excludedId
                );

        if (duplicate) {
            throw new RessourceAlreadyExistException(
                    "Vous avez déjà un rendez-vous pour ce créneau."
            );
        }
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
