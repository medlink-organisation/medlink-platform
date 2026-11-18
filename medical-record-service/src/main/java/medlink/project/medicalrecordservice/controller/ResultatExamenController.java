package medlink.project.medicalrecordservice.controller;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.service.DownloadedDocument;
import medlink.project.medicalrecordservice.service.ResultatExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ResultatExamenController {

    private final ResultatExamenService service;

    @PostMapping(
            path = "/{dossierId}/exam-results",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<ResultatExamenDto> upload(
            @PathVariable Long dossierId,
            @RequestParam String typeExamen,
            @RequestParam LocalDate dateExamen,
            @RequestParam(required = false) String conclusion,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                service.uploadResultat(dossierId, typeExamen, dateExamen, conclusion, file)
        );
    }

    @GetMapping("/exam-results/{id}")
    public ResultatExamenDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/{dossierId}/exam-results")
    public Page<ResultatExamenDto> getByDossier(
            @PathVariable Long dossierId,
            Pageable pageable
    ) {
        return service.getByDossier(dossierId, pageable);
    }

    @GetMapping("/exam-results/{id}/download")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable UUID id) {
        DownloadedDocument document = service.download(id);
        StreamingResponseBody body = outputStream -> {
            try (var inputStream = document.inputStream()) {
                inputStream.transferTo(outputStream);
            }
        };

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(document.filename(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(document.size())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore())
                .body(body);
    }

    @DeleteMapping("/exam-results/{id}")
    @PreAuthorize("hasRole('PRATICIEN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
