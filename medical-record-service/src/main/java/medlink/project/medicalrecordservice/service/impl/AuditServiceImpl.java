package medlink.project.medicalrecordservice.service.impl;

import jakarta.persistence.EntityManager;
import medlink.project.auditcore.model.AuditedEntityRegistry;
import medlink.project.auditcore.service.EnversAuditServiceSupport;
import medlink.project.medicalrecordservice.service.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuditServiceImpl extends EnversAuditServiceSupport implements AuditService {

    public AuditServiceImpl(EntityManager entityManager, AuditedEntityRegistry registry) {
        super(entityManager, registry, "medical-record-service");
    }
}
