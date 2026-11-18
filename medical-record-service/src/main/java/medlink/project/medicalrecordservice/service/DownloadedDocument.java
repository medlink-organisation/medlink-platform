package medlink.project.medicalrecordservice.service;

import java.io.InputStream;

public record DownloadedDocument(
        InputStream inputStream,
        String filename,
        String contentType,
        long size
) {
}
