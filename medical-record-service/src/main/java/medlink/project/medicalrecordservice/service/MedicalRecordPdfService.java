package medlink.project.medicalrecordservice.service;

import lombok.RequiredArgsConstructor;
import medlink.project.medicalrecordservice.entities.CompteRendu;
import medlink.project.medicalrecordservice.entities.DossierMedical;
import medlink.project.medicalrecordservice.entities.ResultatExamen;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import medlink.project.medicalrecordservice.repository.CompteRenduRepository;
import medlink.project.medicalrecordservice.repository.ResultatExamenRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordPdfService {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ResultatExamenRepository resultatRepository;
    private final CompteRenduRepository compteRenduRepository;

    public DownloadedDocument generate(DossierMedical dossier) {
        List<ResultatExamen> resultats = resultatRepository
                .findByDossierMedicalIdOrderByDateExamenDesc(dossier.getId());
        List<CompteRendu> comptesRendus = compteRenduRepository
                .findByDossierMedicalIdOrderByDateRedactionDesc(dossier.getId());

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(document);
            writer.heading("Dossier medical MedLink");
            writer.line("Patient : " + dossier.getPatientId());
            writer.line("Dossier : " + dossier.getId());
            writer.line("Cree le : " + dossier.getDateCreation().format(DATE_TIME));
            writer.line("Derniere modification : " + dossier.getDerniereModification().format(DATE_TIME));

            writer.heading("Resultats d'examens");
            if (resultats.isEmpty()) {
                writer.line("Aucun resultat d'examen.");
            }
            for (ResultatExamen resultat : resultats) {
                writer.line(resultat.getDateExamen().format(DATE) + " - " + resultat.getTypeExamen());
                if (resultat.getConclusion() != null) {
                    writer.line("Conclusion : " + resultat.getConclusion());
                }
            }

            writer.heading("Comptes rendus de consultation");
            if (comptesRendus.isEmpty()) {
                writer.line("Aucun compte rendu.");
            }
            for (CompteRendu compteRendu : comptesRendus) {
                writer.line(compteRendu.getDateRedaction().format(DATE_TIME)
                        + " - Praticien " + compteRendu.getPraticienId());
                writer.line(compteRendu.getContenu());
            }
            writer.close();
            document.save(output);
            byte[] bytes = output.toByteArray();
            return new DownloadedDocument(
                    new ByteArrayInputStream(bytes),
                    "dossier-medical-" + dossier.getId() + ".pdf",
                    "application/pdf",
                    bytes.length
            );
        } catch (IOException exception) {
            throw new BusinessValidationException(
                    "Impossible de generer le PDF du dossier medical."
            );
        }
    }

    private static final class PdfWriter {
        private static final float MARGIN = 50;
        private static final float LINE_HEIGHT = 15;

        private final PDDocument document;
        private final PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        private final PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        private PDPageContentStream content;
        private float y;

        private PdfWriter(PDDocument document) throws IOException {
            this.document = document;
            newPage();
        }

        private void heading(String value) throws IOException {
            ensureSpace(30);
            write(normalize(value), bold, 14);
            y -= 8;
        }

        private void line(String value) throws IOException {
            for (String line : wrap(normalize(value), 95)) {
                ensureSpace(LINE_HEIGHT);
                write(line, regular, 10);
            }
            y -= 3;
        }

        private void write(String value, PDType1Font font, float size) throws IOException {
            content.beginText();
            content.setFont(font, size);
            content.newLineAtOffset(MARGIN, y);
            content.showText(value);
            content.endText();
            y -= LINE_HEIGHT;
        }

        private void ensureSpace(float required) throws IOException {
            if (y - required < MARGIN) {
                content.close();
                newPage();
            }
        }

        private void newPage() throws IOException {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = page.getMediaBox().getHeight() - MARGIN;
        }

        private void close() throws IOException {
            content.close();
        }

        private static String normalize(String value) {
            if (value == null) {
                return "";
            }
            String ascii = Normalizer.normalize(value, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");
            return ascii.replaceAll("[^\\x20-\\x7E]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
        }

        private static List<String> wrap(String value, int max) {
            if (value.length() <= max) {
                return List.of(value);
            }
            java.util.ArrayList<String> lines = new java.util.ArrayList<>();
            String remaining = value;
            while (remaining.length() > max) {
                int split = remaining.lastIndexOf(' ', max);
                if (split <= 0) {
                    split = max;
                }
                lines.add(remaining.substring(0, split).trim());
                remaining = remaining.substring(split).trim();
            }
            if (!remaining.isBlank()) {
                lines.add(remaining);
            }
            return lines;
        }
    }
}
