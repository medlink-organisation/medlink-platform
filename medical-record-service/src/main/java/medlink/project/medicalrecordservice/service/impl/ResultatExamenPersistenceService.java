package medlink.project.medicalrecordservice.service.impl;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.entities.DocumentMedical;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.mapper.ResultatExamenMapper;
import medlink.project.medicalrecordservice.repository.DocumentMedicalRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import medlink.project.medicalrecordservice.validator.DocumentMedicalValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResultatExamenPersistenceService {

    private final DocumentMedicalRepository documentRepository;
    private final ResultatExamenRepository resultatRepository;
    private final DocumentMedicalValidator documentValidator;
    private final ResultatExamenMapper resultatMapper;

    @Transactional
    public ResultatExamenDto persistUploadedResult(
            DossierMedical dossier,
            String typeExamen,
            LocalDate dateExamen,
            String conclusion,
            String filename,
            long size,
            String checksum,
            String objectKey
    ) {
        documentValidator.validateMetadata(
                filename,
                "application/pdf",
                objectKey,
                checksum,
                size
        );

        DocumentMedical document = DocumentMedical.builder()
                .dossierMedical(dossier)
                .nomFichier(filename)
                .typeMime("application/pdf")
                .objectKey(objectKey)
                .checksum(checksum)
                .tailleOctets(size)
                .build();
        DocumentMedical savedDocument = documentRepository.save(document);

        ResultatExamen resultat = ResultatExamen.builder()
                .dossierMedical(dossier)
                .document(savedDocument)
                .typeExamen(typeExamen)
                .dateExamen(dateExamen)
                .conclusion(conclusion)
                .build();

        return resultatMapper.toDto(resultatRepository.saveAndFlush(resultat));
    }
}
