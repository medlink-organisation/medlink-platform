package medlink.project.workflowservice.delegate;

import lombok.RequiredArgsConstructor;
import medlink.project.workflowservice.client.RendezVousGrpcClient;
import medlink.project.workflowservice.client.MedicalRecordGrpcClient;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("finaliserConsultationDelegate")
@RequiredArgsConstructor
public class FinaliserConsultationDelegate implements JavaDelegate {

    private final RendezVousGrpcClient rendezVousGrpcClient;
    private final MedicalRecordGrpcClient medicalRecordGrpcClient;

    @Override
    public void execute(DelegateExecution execution) {
        String rendezVousId = requiredString(execution, "rdvId");
        boolean controleRequis = requiredBoolean(execution, "controleRequis");
        long praticienId = requiredLong(execution, "praticienId");
        String operationPrefix = execution.getProcessInstanceId()
                + ":" + execution.getCurrentActivityId();

        var rendezVous = rendezVousGrpcClient.getById(rendezVousId);
        if (rendezVous.getPraticienId() != praticienId) {
            throw new IllegalStateException("Le praticien n'est pas associé au rendez-vous.");
        }

        var dossier = medicalRecordGrpcClient.ensureForPatient(
                rendezVous.getPatientId(), operationPrefix + ":record");
        medicalRecordGrpcClient.saveReport(
                dossier.getRecordId(), rendezVous.getId(), praticienId,
                rendezVous.getCliniqueId(),
                java.time.Instant.ofEpochSecond(
                        rendezVous.getStartAt().getSeconds(), rendezVous.getStartAt().getNanos()),
                requiredString(execution, "motifConsultation"),
                requiredString(execution, "symptomes"),
                requiredString(execution, "diagnostic"),
                requiredString(execution, "observations"),
                requiredString(execution, "traitementRecommande"),
                requiredString(execution, "examensComplementaires"),
                requiredString(execution, "recommandations"),
                controleRequis,
                operationPrefix + ":report"
        );

        rendezVousGrpcClient.updatePresence(
                rendezVousId,
                true,
                operationPrefix + ":presence"
        );
        rendezVousGrpcClient.completeConsultation(
                rendezVousId,
                controleRequis,
                operationPrefix + ":complete"
        );
        execution.setVariable("patientPresent", true);
    }

    private long requiredLong(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Number number && number.longValue() > 0) {
            return number.longValue();
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }

    private String requiredString(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof String string && !string.isBlank()) {
            return string;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }

    private boolean requiredBoolean(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Boolean bool) {
            return bool;
        }
        throw new IllegalArgumentException(name + " obligatoire");
    }
}
