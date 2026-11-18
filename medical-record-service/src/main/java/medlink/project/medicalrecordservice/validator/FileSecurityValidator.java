package medlink.project.medicalrecordservice.validator;

import medlink.project.medicalrecordservice.exception.FileTooLargeException;
import medlink.project.medicalrecordservice.exception.InvalidFileException;
import medlink.project.medicalrecordservice.config.properties.MedicalFileProperties;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;

@Component
public class FileSecurityValidator {

    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final int MAX_COS_OBJECTS = 100_000;

    private final long maxPdfSizeBytes;
    private final DefaultDetector detector = new DefaultDetector();

    public FileSecurityValidator(MedicalFileProperties properties) {
        this.maxPdfSizeBytes = properties.maxPdfSizeBytes();
    }

    public void validatePdf(MultipartFile file) {
        validateAndReadPdf(file);
    }

    public byte[] validateAndReadPdf(MultipartFile file) {
        validateBasicMetadata(file);

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException exception) {
            throw new InvalidFileException("Le fichier PDF ne peut pas être lu.", exception);
        }
        if (bytes.length == 0) {
            throw new InvalidFileException("Le fichier PDF est vide.");
        }
        if (bytes.length > maxPdfSizeBytes) {
            throw new FileTooLargeException(
                    "Le fichier dépasse la taille maximale autorisée de " + maxPdfSizeBytes + " octets."
            );
        }

        validateDetectedType(bytes);
        validatePdfStructure(bytes);
        return bytes;
    }

    private void validateBasicMetadata(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() <= 0) {
            throw new InvalidFileException("Le fichier PDF est vide.");
        }
        if (file.getSize() > maxPdfSizeBytes) {
            throw new FileTooLargeException(
                    "Le fichier dépasse la taille maximale autorisée de " + maxPdfSizeBytes + " octets."
            );
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            throw new InvalidFileException("Seuls les fichiers portant l'extension .pdf sont acceptés.");
        }
        if (!PDF_MIME_TYPE.equalsIgnoreCase(file.getContentType())) {
            throw new InvalidFileException("Le type MIME déclaré doit être application/pdf.");
        }
    }

    private void validateDetectedType(byte[] bytes) {
        try (TikaInputStream inputStream = TikaInputStream.get(bytes)) {
            MediaType detected = detector.detect(inputStream, new Metadata());
            if (!PDF_MIME_TYPE.equals(detected.toString())) {
                throw new InvalidFileException("Le contenu réel du fichier n'est pas un PDF.");
            }
        } catch (IOException exception) {
            throw new InvalidFileException("Le type réel du fichier ne peut pas être déterminé.", exception);
        }
    }

    private void validatePdfStructure(byte[] bytes) {
        try (PDDocument document = Loader.loadPDF(bytes)) {
            if (document.isEncrypted()) {
                throw new InvalidFileException("Les PDF chiffrés ou protégés par mot de passe sont refusés.");
            }
            if (document.getNumberOfPages() == 0) {
                throw new InvalidFileException("Le PDF ne contient aucune page.");
            }
            rejectActiveContent(document.getDocument().getTrailer());
        } catch (InvalidPasswordException exception) {
            throw new InvalidFileException(
                    "Les PDF chiffrés ou protégés par mot de passe sont refusés.",
                    exception
            );
        } catch (InvalidFileException exception) {
            throw exception;
        } catch (IOException | RuntimeException exception) {
            throw new InvalidFileException("Le PDF est corrompu ou son analyse est incomplète.", exception);
        }
    }

    private void rejectActiveContent(COSBase root) {
        Set<COSBase> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        inspectCosObject(root, visited);
    }

    private void inspectCosObject(COSBase value, Set<COSBase> visited) {
        if (value == null || visited.contains(value)) {
            return;
        }
        if (visited.size() >= MAX_COS_OBJECTS) {
            throw new InvalidFileException("Le PDF est trop complexe pour être analysé de façon sûre.");
        }
        visited.add(value);

        if (value instanceof COSObject object) {
            inspectCosObject(object.getObject(), visited);
            return;
        }
        if (value instanceof COSArray array) {
            for (COSBase item : array) {
                inspectCosObject(item, visited);
            }
            return;
        }
        if (!(value instanceof COSDictionary dictionary)) {
            return;
        }

        rejectDangerousDictionary(dictionary);
        for (COSName key : dictionary.keySet()) {
            inspectCosObject(dictionary.getDictionaryObject(key), visited);
        }
    }

    private void rejectDangerousDictionary(COSDictionary dictionary) {
        if (containsAnyKey(dictionary, "OpenAction", "AA")) {
            throw new InvalidFileException("Le PDF contient une action automatique interdite.");
        }
        if (containsAnyKey(dictionary, "EmbeddedFiles", "EF")) {
            throw new InvalidFileException("Les fichiers joints intégrés au PDF sont interdits.");
        }

        String type = nameValue(dictionary, "Type");
        if ("EmbeddedFile".equals(type) || "Filespec".equals(type)) {
            throw new InvalidFileException("Les fichiers joints intégrés au PDF sont interdits.");
        }

        String action = nameValue(dictionary, "S");
        if ("JavaScript".equals(action)
                || "Launch".equals(action)
                || "SubmitForm".equals(action)
                || "ImportData".equals(action)
                || "GoToR".equals(action)) {
            throw new InvalidFileException("Le PDF contient une fonctionnalité active interdite.");
        }
        if (dictionary.containsKey(COSName.getPDFName("JS"))) {
            throw new InvalidFileException("Le JavaScript embarqué dans un PDF est interdit.");
        }
    }

    private boolean containsAnyKey(COSDictionary dictionary, String... names) {
        for (String name : names) {
            if (dictionary.containsKey(COSName.getPDFName(name))) {
                return true;
            }
        }
        return false;
    }

    private String nameValue(COSDictionary dictionary, String key) {
        COSBase value = dictionary.getDictionaryObject(COSName.getPDFName(key));
        return value instanceof COSName name ? name.getName() : null;
    }
}
