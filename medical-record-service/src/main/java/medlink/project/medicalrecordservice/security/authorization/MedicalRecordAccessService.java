package medlink.project.medicalrecordservice.security.authorization;

import java.util.UUID;

public interface MedicalRecordAccessService {

    void checkCanCreateForPatient(Long patientId);

    void checkCanRead(Long dossierId);

    void checkCanWrite(Long dossierId);

    void checkCanAttachResult(Long dossierId, UUID rendezVousId);

    Long requireRecordIdForAppointment(UUID rendezVousId);

    void checkCanListAll();

    void checkCanListByPractitioner(Long practitionerId);
}
