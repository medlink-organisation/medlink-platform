package medlink.project.medicalrecordservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.service.DownloadedDocument;
import medlink.project.medicalrecordservice.service.MedicalRecordViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/medical-records/appointments/{rendezVousId}")
@PreAuthorize("hasRole('PRATICIEN')")
@RequiredArgsConstructor
public class PractitionerMedicalRecordController {

    private final MedicalRecordViewService service;

    @GetMapping
    public DossierMedicalDto getRecord(@PathVariable UUID rendezVousId) {
        return service.getForAppointment(rendezVousId);
    }

    @GetMapping("/exam-results")
    public Page<ResultatExamenDto> getResults(
            @PathVariable UUID rendezVousId,
            Pageable pageable
    ) {
        return service.getResultsForAppointment(rendezVousId, pageable);
    }

    @GetMapping("/pdf")
    public ResponseEntity<StreamingResponseBody> download(
            @PathVariable UUID rendezVousId
    ) {
        return pdf(service.downloadForAppointment(rendezVousId));
    }

    private ResponseEntity<StreamingResponseBody> pdf(DownloadedDocument document) {
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
