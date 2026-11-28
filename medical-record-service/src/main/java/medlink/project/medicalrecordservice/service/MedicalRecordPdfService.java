package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.dto.CompteRenduDto;
import medlink.project.medicalrecordservice.dto.DossierMedicalDto;
import medlink.project.medicalrecordservice.dto.ResultatExamenDto;
import medlink.project.medicalrecordservice.exception.BusinessValidationException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MedicalRecordPdfService {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DownloadedDocument generate(DossierMedicalDto dossier) {
        LocalDateTime generatedAt = LocalDateTime.now();
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(document, generatedAt);
            writer.documentHeader();
            writer.keyValue("Date de génération", generatedAt.format(DATE_TIME));
            writer.keyValue("Référence du dossier", value(dossier.getId()));
            writer.gap(8);
            writePatient(writer, dossier);
            writeSummary(writer, dossier);
            writeConsultations(writer, dossier.getComptesRendus());
            writeResults(writer, dossier.getResultatsExamens());
            writer.finish();
            document.save(output);
            byte[] bytes = output.toByteArray();
            return new DownloadedDocument(new ByteArrayInputStream(bytes),
                    "dossier-medical-" + dossier.getId() + ".pdf", "application/pdf", bytes.length);
        } catch (IOException exception) {
            throw new BusinessValidationException("Impossible de générer le PDF du dossier médical.");
        }
    }

    private void writePatient(PdfWriter writer, DossierMedicalDto dossier) throws IOException {
        writer.section("Informations du patient");
        writer.infoTable(List.of(
                new PdfField("Nom et prénom", fullName(dossier.getPatientPrenom(), dossier.getPatientNom())),
                new PdfField("Date de naissance", dossier.getPatientDateNaissance()),
                new PdfField("Sexe", dossier.getPatientSexe()),
                new PdfField("Téléphone", dossier.getPatientTelephone()),
                new PdfField("Adresse électronique", dossier.getPatientEmail()),
                new PdfField("Date de création", format(dossier.getDateCreation()))
        ));
    }

    private void writeSummary(PdfWriter writer, DossierMedicalDto dossier) throws IOException {
        List<CompteRenduDto> reports = safeList(dossier.getComptesRendus());
        List<ResultatExamenDto> results = safeList(dossier.getResultatsExamens());
        LocalDateTime lastConsultation = reports.stream()
                .map(report -> report.getDateConsultation() == null
                        ? report.getDateRedaction() : report.getDateConsultation())
                .filter(java.util.Objects::nonNull)
                .max(Comparator.naturalOrder()).orElse(null);
        writer.section("Synthèse médicale");
        writer.infoTable(List.of(
                new PdfField("Consultations", String.valueOf(reports.size())),
                new PdfField("Résultats d'examens", String.valueOf(results.size())),
                new PdfField("Dernière consultation", format(lastConsultation)),
                new PdfField("Dernière mise à jour", format(dossier.getDerniereModification()))
        ));
    }

    private void writeConsultations(PdfWriter writer, List<CompteRenduDto> values) throws IOException {
        writer.section("Historique des consultations");
        List<CompteRenduDto> reports = safeList(values).stream()
                .sorted(Comparator.comparing(
                        (CompteRenduDto report) -> report.getDateConsultation() == null
                                ? report.getDateRedaction() : report.getDateConsultation(),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        if (reports.isEmpty()) {
            writer.empty("Aucune consultation enregistrée.");
            return;
        }
        for (CompteRenduDto report : reports) {
            LocalDateTime consultationDate = report.getDateConsultation() == null
                    ? report.getDateRedaction() : report.getDateConsultation();
            writer.consultationTitle(format(consultationDate));
            writer.labeledText("Praticien", fullName(report.getPraticienPrenom(), report.getPraticienNom()));
            writer.labeledText("Spécialité", report.getSpecialiteNom());
            writer.labeledText("Clinique", report.getCliniqueNom());
            writer.labeledText("Motif", report.getMotif());
            writer.labeledText("Symptômes", report.getSymptomes());
            writer.labeledText("Diagnostic", report.getDiagnostic());
            writer.labeledText("Observations", report.getObservations());
            writer.labeledText("Traitement", report.getTraitementRecommande());
            writer.labeledText("Examens complémentaires", report.getExamensComplementaires());
            writer.labeledText("Recommandations", report.getRecommandations());
            writer.labeledText("Compte rendu", report.getContenu());
            if (Boolean.TRUE.equals(report.getControleRequis())) {
                writer.labeledText("Consultation de contrôle", "Nécessaire");
            }
            writer.gap(7);
        }
    }

    private void writeResults(PdfWriter writer, List<ResultatExamenDto> values) throws IOException {
        writer.section("Résultats d'examens");
        List<ResultatExamenDto> results = safeList(values).stream()
                .sorted(Comparator.comparing(ResultatExamenDto::getDateExamen,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        if (results.isEmpty()) {
            writer.empty("Aucun résultat d'examen enregistré.");
        } else {
            writer.resultTable(results);
        }
    }

    private static String fullName(String firstName, String lastName) {
        String result = ((firstName == null ? "" : firstName.trim()) + " "
                + (lastName == null ? "" : lastName.trim())).trim();
        return result.isBlank() ? null : result;
    }

    private static String format(LocalDateTime date) {
        return date == null ? null : date.format(DATE_TIME);
    }

    private static String value(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : values;
    }

    private record PdfField(String label, String value) {
    }

    private static final class PdfWriter {
        private static final float MARGIN = 46;
        private static final float FOOTER_HEIGHT = 31;
        private static final float WIDTH = PDRectangle.A4.getWidth() - (2 * MARGIN);
        private static final Color NAVY = new Color(15, 42, 78);
        private static final Color BLUE = new Color(37, 99, 235);
        private static final Color MUTED = new Color(82, 96, 117);
        private static final Color BORDER = new Color(213, 220, 230);
        private static final Color SURFACE = new Color(246, 248, 251);

        private final PDDocument document;
        private final LocalDateTime generatedAt;
        private final PDType1Font regular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        private final PDType1Font bold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        private PDPageContentStream content;
        private float y;

        private PdfWriter(PDDocument document, LocalDateTime generatedAt) throws IOException {
            this.document = document;
            this.generatedAt = generatedAt;
            newPage();
        }

        private void documentHeader() throws IOException {
            logo(MARGIN, y - 30, 30);
            text("MedLink", MARGIN + 41, y - 9, bold, 13, NAVY);
            text("Dossier médical", MARGIN + 41, y - 27, bold, 19, NAVY);
            text("DOCUMENT MÉDICAL CONFIDENTIEL", MARGIN + 310, y - 9, bold, 8, MUTED);
            y -= 46;
            rule();
            y -= 12;
        }

        private void logo(float x, float bottom, float size) throws IOException {
            content.setNonStrokingColor(BLUE);
            content.addRect(x, bottom, size, size);
            content.fill();
            text("M", x + 6.5f, bottom + 9, bold, 14, Color.WHITE);
            float badgeRadius = 5.2f;
            float centerX = x + size - 2;
            float centerY = bottom + size - 2;
            float control = badgeRadius * .5522848f;
            content.setNonStrokingColor(new Color(96, 165, 250));
            content.moveTo(centerX + badgeRadius, centerY);
            content.curveTo(centerX + badgeRadius, centerY + control,
                    centerX + control, centerY + badgeRadius, centerX, centerY + badgeRadius);
            content.curveTo(centerX - control, centerY + badgeRadius,
                    centerX - badgeRadius, centerY + control, centerX - badgeRadius, centerY);
            content.curveTo(centerX - badgeRadius, centerY - control,
                    centerX - control, centerY - badgeRadius, centerX, centerY - badgeRadius);
            content.curveTo(centerX + control, centerY - badgeRadius,
                    centerX + badgeRadius, centerY - control, centerX + badgeRadius, centerY);
            content.fill();
            content.setStrokingColor(Color.WHITE);
            content.setLineWidth(1.2f);
            content.moveTo(centerX - 2.8f, centerY);
            content.lineTo(centerX + 2.8f, centerY);
            content.moveTo(centerX, centerY - 2.8f);
            content.lineTo(centerX, centerY + 2.8f);
            content.stroke();
        }

        private void section(String title) throws IOException {
            ensure(70);
            text(title, MARGIN, y, bold, 13, NAVY);
            y -= 9;
            content.setStrokingColor(BLUE);
            content.setLineWidth(1.2f);
            content.moveTo(MARGIN, y);
            content.lineTo(MARGIN + 34, y);
            content.stroke();
            y -= 16;
        }

        private void consultationTitle(String date) throws IOException {
            ensure(82);
            content.setNonStrokingColor(SURFACE);
            content.addRect(MARGIN, y - 22, WIDTH, 26);
            content.fill();
            text("Consultation" + (date == null ? "" : " · " + date),
                    MARGIN + 9, y - 13, bold, 10.5f, NAVY);
            y -= 31;
        }

        private void keyValue(String label, String value) throws IOException {
            if (blank(value)) return;
            ensure(16);
            text(label + " :", MARGIN, y, bold, 9, MUTED);
            text(value, MARGIN + 112, y, regular, 9, NAVY);
            y -= 15;
        }

        private void infoTable(List<PdfField> source) throws IOException {
            List<PdfField> fields = source.stream().filter(field -> !blank(field.value())).toList();
            if (fields.isEmpty()) {
                empty("Aucune information disponible.");
                return;
            }
            for (PdfField field : fields) {
                List<String> lines = wrap(field.value(), regular, 9.5f, WIDTH - 165);
                float height = Math.max(25, 12 + lines.size() * 12);
                ensure(height);
                content.setStrokingColor(BORDER);
                content.setLineWidth(.5f);
                content.addRect(MARGIN, y - height, WIDTH, height);
                content.stroke();
                text(field.label(), MARGIN + 8, y - 16, bold, 9, MUTED);
                float lineY = y - 16;
                for (String line : lines) {
                    text(line, MARGIN + 158, lineY, regular, 9.5f, NAVY);
                    lineY -= 12;
                }
                y -= height;
            }
            y -= 12;
        }

        private void labeledText(String label, String value) throws IOException {
            if (blank(value)) return;
            List<String> lines = wrap(value, regular, 9.5f, WIDTH - 116);
            int offset = 0;
            while (offset < lines.size()) {
                int availableLines = (int) Math.floor(
                        (y - MARGIN - FOOTER_HEIGHT - 3) / 13f
                );
                if (availableLines < 2) {
                    newPage();
                    availableLines = (int) Math.floor(
                            (y - MARGIN - FOOTER_HEIGHT - 3) / 13f
                    );
                }
                int lineCount = Math.min(lines.size() - offset, availableLines);
                text(label + (offset == 0 ? " :" : " (suite) :"),
                        MARGIN + 7, y, bold, 9, MUTED);
                float lineY = y;
                for (int index = offset; index < offset + lineCount; index++) {
                    text(lines.get(index), MARGIN + 116, lineY, regular, 9.5f, NAVY);
                    lineY -= 13;
                }
                y -= Math.max(16, lineCount * 13f + 2);
                offset += lineCount;
                if (offset < lines.size()) {
                    newPage();
                }
            }
        }

        private void resultTable(List<ResultatExamenDto> results) throws IOException {
            float[] widths = {66, 112, 116, WIDTH - 294};
            resultHeader(widths);
            for (ResultatExamenDto result : results) {
                List<List<String>> allCells = List.of(
                        wrap(result.getDateExamen() == null ? null : result.getDateExamen().format(DATE), regular, 8.5f, widths[0] - 10),
                        wrap(result.getTypeExamen(), regular, 8.5f, widths[1] - 10),
                        wrap(result.getPraticienNomComplet(), regular, 8.5f, widths[2] - 10),
                        wrap(result.getConclusion(), regular, 8.5f, widths[3] - 10));
                int totalLines = allCells.stream().mapToInt(List::size).max().orElse(1);
                int offset = 0;
                while (offset < totalLines) {
                    int availableLines = (int) Math.floor(
                            (y - MARGIN - FOOTER_HEIGHT - 10) / 11f
                    );
                    if (availableLines < 2) {
                        newPage();
                        section("Résultats d'examens (suite)");
                        resultHeader(widths);
                        availableLines = (int) Math.floor(
                                (y - MARGIN - FOOTER_HEIGHT - 10) / 11f
                        );
                    }
                    int lineCount = Math.min(totalLines - offset, availableLines);
                    List<List<String>> cells = new ArrayList<>();
                    for (List<String> cell : allCells) {
                        int from = Math.min(offset, cell.size());
                        int to = Math.min(offset + lineCount, cell.size());
                        cells.add(from < to ? cell.subList(from, to) : List.of(""));
                    }
                    float height = Math.max(28, 10 + lineCount * 11f);
                    drawRow(cells, widths, height, false);
                    offset += lineCount;
                    if (offset < totalLines) {
                        newPage();
                        section("Résultats d'examens (suite)");
                        resultHeader(widths);
                    }
                }
            }
            y -= 8;
        }

        private void resultHeader(float[] widths) throws IOException {
            ensure(28);
            drawRow(List.of(List.of("Date"), List.of("Type d'examen"),
                    List.of("Praticien"), List.of("Conclusion")), widths, 26, true);
        }

        private void drawRow(List<List<String>> cells, float[] widths, float height, boolean header)
                throws IOException {
            float x = MARGIN;
            content.setNonStrokingColor(header ? NAVY : Color.WHITE);
            content.addRect(MARGIN, y - height, WIDTH, height);
            content.fill();
            content.setStrokingColor(BORDER);
            content.setLineWidth(.5f);
            for (int index = 0; index < widths.length; index++) {
                content.addRect(x, y - height, widths[index], height);
                content.stroke();
                float lineY = y - 16;
                for (String line : cells.get(index)) {
                    text(line, x + 5, lineY, header ? bold : regular,
                            header ? 8.2f : 8.5f, header ? Color.WHITE : NAVY);
                    lineY -= 11;
                }
                x += widths[index];
            }
            y -= height;
        }

        private void empty(String value) throws IOException {
            ensure(25);
            text(value, MARGIN + 7, y, regular, 9.5f, MUTED);
            y -= 25;
        }

        private void rule() throws IOException {
            content.setStrokingColor(BORDER);
            content.setLineWidth(.6f);
            content.moveTo(MARGIN, y);
            content.lineTo(MARGIN + WIDTH, y);
            content.stroke();
        }

        private void gap(float value) throws IOException {
            ensure(value);
            y -= value;
        }

        private void ensure(float required) throws IOException {
            if (!hasSpace(required)) newPage();
        }

        private boolean hasSpace(float required) {
            return y - required > MARGIN + FOOTER_HEIGHT;
        }

        private void newPage() throws IOException {
            if (content != null) content.close();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = page.getMediaBox().getHeight() - MARGIN;
        }

        private void finish() throws IOException {
            if (content != null) {
                content.close();
                content = null;
            }
            int count = document.getNumberOfPages();
            for (int index = 0; index < count; index++) {
                try (PDPageContentStream footer = new PDPageContentStream(document,
                        document.getPage(index), PDPageContentStream.AppendMode.APPEND, true, true)) {
                    footer.setStrokingColor(BORDER);
                    footer.setLineWidth(.5f);
                    footer.moveTo(MARGIN, 38);
                    footer.lineTo(MARGIN + WIDTH, 38);
                    footer.stroke();
                    footerText(footer, "MedLink · Document médical confidentiel", MARGIN, 24);
                    footerText(footer, "Page " + (index + 1) + " / " + count, MARGIN + 236, 24);
                    footerText(footer, generatedAt.format(DATE_TIME), MARGIN + 412, 24);
                }
            }
        }

        private void footerText(PDPageContentStream stream, String value, float x, float textY)
                throws IOException {
            stream.beginText();
            stream.setNonStrokingColor(MUTED);
            stream.setFont(regular, 7.5f);
            stream.newLineAtOffset(x, textY);
            stream.showText(safe(value));
            stream.endText();
        }

        private void text(String value, float x, float textY, PDType1Font font, float size, Color color)
                throws IOException {
            content.beginText();
            content.setNonStrokingColor(color);
            content.setFont(font, size);
            content.newLineAtOffset(x, textY);
            content.showText(safe(value));
            content.endText();
        }

        private List<String> wrap(String value, PDType1Font font, float size, float maxWidth)
                throws IOException {
            if (blank(value)) return List.of("—");
            List<String> lines = new ArrayList<>();
            for (String paragraph : safe(value).split("\\R", -1)) {
                if (paragraph.isBlank()) {
                    lines.add("");
                    continue;
                }
                StringBuilder current = new StringBuilder();
                for (String word : paragraph.trim().split("\\s+")) {
                    if (width(word, font, size) > maxWidth) {
                        if (!current.isEmpty()) {
                            lines.add(current.toString());
                            current = new StringBuilder();
                        }
                        for (char character : word.toCharArray()) {
                            String candidate = current.toString() + character;
                            if (!current.isEmpty() && width(candidate, font, size) > maxWidth) {
                                lines.add(current.toString());
                                current = new StringBuilder(String.valueOf(character));
                            } else {
                                current.append(character);
                            }
                        }
                        continue;
                    }
                    String candidate = current.isEmpty() ? word : current + " " + word;
                    if (width(candidate, font, size) <= maxWidth) {
                        current = new StringBuilder(candidate);
                    } else {
                        if (!current.isEmpty()) lines.add(current.toString());
                        current = new StringBuilder(word);
                    }
                }
                if (!current.isEmpty()) lines.add(current.toString());
            }
            return lines.isEmpty() ? List.of("—") : lines;
        }

        private float width(String value, PDType1Font font, float size) throws IOException {
            return font.getStringWidth(safe(value)) / 1000f * size;
        }

        private static boolean blank(String value) {
            return value == null || value.isBlank();
        }

        private static String safe(String value) {
            if (value == null) return "";
            return value.replace('’', '\'').replace('“', '"').replace('”', '"')
                    .replace("œ", "oe").replace("Œ", "OE").replace("…", "...")
                    .replaceAll("[\\p{Cc}&&[^\\r\\n\\t]]", " ");
        }
    }
}
