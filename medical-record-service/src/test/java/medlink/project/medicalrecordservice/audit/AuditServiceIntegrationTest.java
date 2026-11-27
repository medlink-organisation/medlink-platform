package medlink.project.medicalrecordservice.audit;

import medlink.project.auditcore.dto.ActorType;
import medlink.project.auditcore.dto.AuditOperation;
import medlink.project.auditcore.dto.AuditRevisionDto;
import medlink.project.auditcore.service.AuditFilter;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.repository.DossierMedicalRepository;
import medlink.project.medicalrecordservice.service.AuditService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.application.name=medical-record-service",
        "spring.cloud.bus.enabled=false",
        "spring.cloud.vault.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
class AuditServiceIntegrationTest {

    @Autowired
    private DossierMedicalRepository dossierRepository;

    @Autowired
    private DocumentMedicalRepository documentRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactions;

    @BeforeEach
    void setUp() {
        transactions = new TransactionTemplate(transactionManager);
        SecurityContextHolder.getContext().setAuthentication(jwt("keycloak-user-id", "audit-admin"));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void readsCreateUpdateDeleteHistoryAndComparesRevisions() {
        Long id = transactions.execute(status -> dossierRepository.saveAndFlush(
                DossierMedical.builder().patientId(7001L).build()
        ).getId());

        transactions.executeWithoutResult(status -> {
            DossierMedical dossier = dossierRepository.findById(id).orElseThrow();
            dossier.setPatientId(7002L);
            dossierRepository.saveAndFlush(dossier);
        });
        transactions.executeWithoutResult(status -> dossierRepository.deleteById(id));

        Page<AuditRevisionDto> history = auditService.getHistory(
                "dossier-medical", id.toString(), PageRequest.of(0, 10)
        );

        assertEquals(3, history.getTotalElements());
        assertEquals(
                java.util.List.of(AuditOperation.DEL, AuditOperation.MOD, AuditOperation.ADD),
                history.getContent().stream().map(AuditRevisionDto::operation).toList()
        );
        assertTrue(history.getContent().stream().allMatch(item -> "keycloak-user-id".equals(item.userId())));
        assertTrue(history.getContent().stream().allMatch(item -> item.actorType() == ActorType.USER));

        long addRevision = history.getContent().get(2).revisionId();
        long modRevision = history.getContent().get(1).revisionId();
        AuditRevisionDto comparison = auditService.compare(
                "dossier-medical", id.toString(), addRevision, modRevision
        );
        assertFalse(comparison.changes().isEmpty());
        assertTrue(comparison.changes().stream().allMatch(change ->
                change.fieldName().equals("dateCreation")
                        || change.fieldName().equals("derniereModification")
        ));
    }

    @Test
    void paginatesFiltersAndNeverExposesMedicalSecrets() {
        Long dossierId = transactions.execute(status -> dossierRepository.saveAndFlush(
                DossierMedical.builder().patientId(7101L).build()
        ).getId());
        Long documentId = transactions.execute(status -> {
            DossierMedical dossier = dossierRepository.findById(dossierId).orElseThrow();
            return documentRepository.saveAndFlush(DocumentMedical.builder()
                    .dossierMedical(dossier)
                    .nomFichier("secret-medical.pdf")
                    .typeMime("application/pdf")
                    .objectKey("private/patient/secret-medical.pdf")
                    .checksum("a".repeat(64))
                    .tailleOctets(1024L)
                    .build()).getId();
        });

        Page<AuditRevisionDto> page = auditService.search(
                new AuditFilter("document-medical", null, null, "keycloak-user-id",
                        AuditOperation.ADD, null, null),
                PageRequest.of(0, 1)
        );

        assertEquals(1, page.getSize());
        assertEquals(documentId.toString(), page.getContent().getFirst().entityId());
        assertTrue(page.getContent().getFirst().changes().stream().noneMatch(change ->
                java.util.Set.of("nomFichier", "objectKey", "checksum", "contenu", "conclusion")
                        .contains(change.fieldName())
        ));
    }

    @Test
    void rollbackDoesNotPersistARevision() {
        long before = auditService.search(
                new AuditFilter("dossier-medical", null, null, null, null, null, null),
                PageRequest.of(0, 1)
        ).getTotalElements();

        transactions.executeWithoutResult(status -> {
            dossierRepository.saveAndFlush(DossierMedical.builder().patientId(7201L).build());
            status.setRollbackOnly();
        });

        long after = auditService.search(
                new AuditFilter("dossier-medical", null, null, null, null, null, null),
                PageRequest.of(0, 1)
        ).getTotalElements();
        assertEquals(before, after);
    }

    private JwtAuthenticationToken jwt(String subject, String username) {
        Instant now = Instant.now();
        Jwt jwt = new Jwt(
                "token", now, now.plusSeconds(300),
                Map.of("alg", "none"),
                Map.of("sub", subject, "preferred_username", username)
        );
        return new JwtAuthenticationToken(
                jwt,
                java.util.List.of(new SimpleGrantedAuthority("AUDIT_READ")),
                username
        );
    }
}
