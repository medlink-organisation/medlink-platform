package medlink.project.medicalrecordservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.security.authorization.AuthenticatedUserService;
import medlink.project.medicalrecordservice.service.DossierMedicalService;
import medlink.project.medicalrecordservice.service.ResultatExamenService;
import medlink.project.medicalrecordservice.service.DownloadedDocument;
import medlink.project.medicalrecordservice.service.MedicalRecordViewService;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical-records/me")
@PreAuthorize("hasRole('PATIENT')")
@RequiredArgsConstructor
public class PatientMedicalRecordController {

    private final AuthenticatedUserService authenticatedUserService;
    private final DossierMedicalService dossierMedicalService;
    private final ResultatExamenService resultatExamenService;
    private final MedicalRecordViewService medicalRecordViewService;

    @GetMapping
    public DossierMedicalDto getMyRecord() {
        return dossierMedicalService.getByPatientId(
                authenticatedUserService.requirePatientId()
        );
    }

    @GetMapping("/exam-results")
    public Page<ResultatExamenDto> getMyExamResults(Pageable pageable) {
        DossierMedicalDto dossier = getMyRecord();
        return resultatExamenService.getByDossier(dossier.getId(), pageable);
    }

    @GetMapping("/pdf")
    public ResponseEntity<StreamingResponseBody> downloadMyRecord() {
        DownloadedDocument document = medicalRecordViewService.downloadMine();
        StreamingResponseBody body = outputStream -> {
            try (var inputStream = document.inputStream()) {
                inputStream.transferTo(outputStream);
            }
        };
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(document.size())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(document.filename(), StandardCharsets.UTF_8)
                        .build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(body);
    }
}
