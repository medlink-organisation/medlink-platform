package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.config.properties.MedicalFileProperties;
import medlink.project.medicalrecordservice.exception.FileTooLargeException;
import medlink.project.medicalrecordservice.exception.InvalidFileException;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileSecurityValidatorTest {

    private final FileSecurityValidator validator = new FileSecurityValidator(
            new MedicalFileProperties(10 * 1024 * 1024)
    );

    @Test
    void acceptsSimpleValidPdf() throws IOException {
        assertDoesNotThrow(() -> validator.validatePdf(pdfFile(createPdf(null))));
    }

    @Test
    void rejectsRenamedNonPdf() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "fake.pdf", "application/pdf", "not a pdf".getBytes(StandardCharsets.UTF_8)
        );
        assertThrows(InvalidFileException.class, () -> validator.validatePdf(file));
    }

    @Test
    void rejectsEmptyFile() {
        assertThrows(
                InvalidFileException.class,
                () -> validator.validatePdf(
                        new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0])
                )
        );
    }

    @Test
    void rejectsOversizedFile() throws IOException {
        byte[] pdf = createPdf(null);
        FileSecurityValidator smallLimitValidator = new FileSecurityValidator(
                new MedicalFileProperties(pdf.length - 1L)
        );

        assertThrows(FileTooLargeException.class, () -> smallLimitValidator.validatePdf(pdfFile(pdf)));
    }

    @Test
    void rejectsCorruptedPdf() {
        byte[] corrupted = "%PDF-1.7\ncorrupted".getBytes(StandardCharsets.US_ASCII);
        assertThrows(InvalidFileException.class, () -> validator.validatePdf(pdfFile(corrupted)));
    }

    @Test
    void rejectsEncryptedPdf() throws IOException {
        byte[] encrypted;
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            document.addPage(new PDPage());
            StandardProtectionPolicy policy = new StandardProtectionPolicy(
                    "owner-secret",
                    "user-secret",
                    new AccessPermission()
            );
            document.protect(policy);
            document.save(output);
            encrypted = output.toByteArray();
        }

        assertThrows(InvalidFileException.class, () -> validator.validatePdf(pdfFile(encrypted)));
    }

    @Test
    void rejectsPdfWithJavaScript() throws IOException {
        byte[] pdf = createPdf(document -> document.getDocumentCatalog().setOpenAction(
                new PDActionJavaScript("app.alert('unsafe')")
        ));

        assertThrows(InvalidFileException.class, () -> validator.validatePdf(pdfFile(pdf)));
    }

    @Test
    void rejectsPdfWithEmbeddedFileMarker() throws IOException {
        byte[] pdf = createPdf(document -> document.getDocumentCatalog().getCOSObject().setItem(
                COSName.getPDFName("EmbeddedFiles"),
                new COSDictionary()
        ));

        assertThrows(InvalidFileException.class, () -> validator.validatePdf(pdfFile(pdf)));
    }

    private byte[] createPdf(PdfCustomizer customizer) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            document.addPage(new PDPage());
            if (customizer != null) {
                customizer.customize(document);
            }
            document.save(output);
            return output.toByteArray();
        }
    }

    private MockMultipartFile pdfFile(byte[] content) {
        return new MockMultipartFile("file", "result.pdf", "application/pdf", content);
    }

    @FunctionalInterface
    private interface PdfCustomizer {
        void customize(PDDocument document) throws IOException;
    }
}
