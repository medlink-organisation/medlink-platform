package medlink.project.medicalrecordservice.service.impl;

import medlink.project.medicalrecordservice.exception.AntivirusUnavailableException;
import medlink.project.medicalrecordservice.exception.FileTooLargeException;
import medlink.project.medicalrecordservice.exception.InfectedFileException;
import medlink.project.medicalrecordservice.config.properties.ClamAvProperties;
import medlink.project.medicalrecordservice.service.ClamAvService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class ClamAvServiceImpl implements ClamAvService {

    private static final int CHUNK_SIZE = 8192;
    private static final int MAX_RESPONSE_SIZE = 4096;

    private final String host;
    private final int port;
    private final int timeout;

    public ClamAvServiceImpl(ClamAvProperties properties) {
        this.host = properties.host();
        this.port = properties.port();
        this.timeout = properties.timeout();
    }

    @Override
    public void scan(InputStream inputStream) {
        if (inputStream == null) {
            throw new AntivirusUnavailableException("Le flux à analyser est indisponible.");
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.setSoTimeout(timeout);

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.write("zINSTREAM\0".getBytes(StandardCharsets.US_ASCII));

            byte[] buffer = new byte[CHUNK_SIZE];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                output.writeInt(read);
                output.write(buffer, 0, read);
            }
            output.writeInt(0);
            output.flush();

            handleResponse(readResponse(socket.getInputStream()));
        } catch (SocketTimeoutException exception) {
            throw new AntivirusUnavailableException("L'analyse antivirus a expiré.", exception);
        } catch (IOException exception) {
            throw new AntivirusUnavailableException("Le service antivirus est indisponible.", exception);
        }
    }

    private String readResponse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        int value;
        while ((value = inputStream.read()) != -1 && value != 0) {
            if (response.size() >= MAX_RESPONSE_SIZE) {
                throw new IOException("Réponse ClamAV anormalement longue");
            }
            response.write(value);
        }
        if (response.size() == 0) {
            throw new IOException("Réponse ClamAV vide");
        }
        return response.toString(StandardCharsets.US_ASCII).trim();
    }

    private void handleResponse(String response) {
        String normalized = response.toUpperCase(Locale.ROOT);
        if (normalized.endsWith(" OK")) {
            return;
        }
        if (normalized.endsWith(" FOUND")) {
            throw new InfectedFileException("Le fichier a été identifié comme malveillant.");
        }
        if (normalized.contains("SIZE LIMIT EXCEEDED")
                || normalized.contains("LIMIT_EXCEEDED")) {
            throw new FileTooLargeException("La limite d'analyse de ClamAV a été dépassée.");
        }
        throw new AntivirusUnavailableException(
                "ClamAV n'a pas terminé l'analyse de manière fiable."
        );
    }
}
