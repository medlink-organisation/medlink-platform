package medlink.project.medicalrecordservice.repository;

import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.config.JpaAuditConfig;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({JpaAuditConfig.class, MedicalRecordRepositoryTest.AuditTestConfig.class})
class MedicalRecordRepositoryTest {

    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;

    @Autowired
    private ResultatExamenRepository resultatExamenRepository;

    @Autowired
    private CompteRenduRepository compteRenduRepository;

    @Autowired
    private DocumentMedicalRepository documentMedicalRepository;

    @Test
    void enforcesUniquePatientDossier() {
        dossierMedicalRepository.saveAndFlush(dossier(42L));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> dossierMedicalRepository.saveAndFlush(dossier(42L))
        );
    }

    @Test
    void enforcesUniqueCompteRenduPerRendezVous() {
        DossierMedical dossier = dossierMedicalRepository.saveAndFlush(dossier(43L));
        UUID rendezVousId = UUID.randomUUID();
        compteRenduRepository.saveAndFlush(compteRendu(dossier, rendezVousId));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> compteRenduRepository.saveAndFlush(compteRendu(dossier, rendezVousId))
        );
    }

    @Test
    void enforcesUniqueObjectKey() {
        DossierMedical dossier = dossierMedicalRepository.saveAndFlush(dossier(44L));
        String objectKey = "patients/44/documents/resultat.pdf";
        documentMedicalRepository.saveAndFlush(document(dossier, objectKey));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> documentMedicalRepository.saveAndFlush(document(dossier, objectKey))
        );
    }

    @Test
    void enforcesOneResultPerDocument() {
        DossierMedical dossier = dossierMedicalRepository.saveAndFlush(dossier(46L));
        DocumentMedical document = documentMedicalRepository.saveAndFlush(
                document(dossier, "medical-documents/shared.pdf")
        );
        resultatExamenRepository.saveAndFlush(examen(dossier, document, "Analyse"));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> resultatExamenRepository.saveAndFlush(examen(dossier, document, "Radiologie"))
        );
    }

    @Test
    void paginatesRelationsByDossierAndExamen() {
        DossierMedical dossier = dossierMedicalRepository.saveAndFlush(dossier(45L));
        DocumentMedical firstDocument = documentMedicalRepository.saveAndFlush(
                document(dossier, "medical-documents/analyse.pdf")
        );
        DocumentMedical secondDocument = documentMedicalRepository.saveAndFlush(
                document(dossier, "medical-documents/radiologie.pdf")
        );
        ResultatExamen first = resultatExamenRepository.saveAndFlush(
                examen(dossier, firstDocument, "Analyse")
        );
        resultatExamenRepository.saveAndFlush(examen(dossier, secondDocument, "Radiologie"));

        Page<ResultatExamen> examens = resultatExamenRepository.findByDossierMedicalId(
                dossier.getId(),
                PageRequest.of(0, 1)
        );
        Page<DocumentMedical> documents = documentMedicalRepository.findByDossierMedicalId(
                dossier.getId(),
                PageRequest.of(0, 10)
        );

        assertEquals(2, examens.getTotalElements());
        assertEquals(1, examens.getContent().size());
        assertEquals(2, documents.getTotalElements());
        assertEquals(firstDocument.getId(), first.getDocument().getId());
    }

    private DossierMedical dossier(Long patientId) {
        return DossierMedical.builder().patientId(patientId).build();
    }

    private ResultatExamen examen(
            DossierMedical dossier,
            DocumentMedical document,
            String type
    ) {
        return ResultatExamen.builder()
                .dossierMedical(dossier)
                .document(document)
                .typeExamen(type)
                .dateExamen(LocalDate.now())
                .conclusion("Conclusion " + type)
                .build();
    }

    private CompteRendu compteRendu(DossierMedical dossier, UUID rendezVousId) {
        return CompteRendu.builder()
                .dossierMedical(dossier)
                .rendezVousId(rendezVousId)
                .praticienId(UUID.randomUUID())
                .contenu("Compte rendu médical")
                .build();
    }

    private DocumentMedical document(
            DossierMedical dossier,
            String objectKey
    ) {
        return DocumentMedical.builder()
                .dossierMedical(dossier)
                .nomFichier("resultat.pdf")
                .typeMime("application/pdf")
                .objectKey(objectKey)
                .checksum("a".repeat(64))
                .tailleOctets(1024L)
                .build();
    }

    @TestConfiguration
    static class AuditTestConfig {

        @Bean(name = "auditorAwareImpl")
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("repository-test");
        }
    }
}
