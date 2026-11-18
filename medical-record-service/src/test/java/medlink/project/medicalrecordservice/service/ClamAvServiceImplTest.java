package medlink.project.medicalrecordservice.service;

import medlink.project.medicalrecordservice.config.properties.ClamAvProperties;
import medlink.project.medicalrecordservice.exception.AntivirusUnavailableException;
import medlink.project.medicalrecordservice.exception.FileTooLargeException;
import medlink.project.medicalrecordservice.exception.InfectedFileException;
import medlink.project.medicalrecordservice.service.impl.ClamAvServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClamAvServiceImplTest {

    private static final byte[] EICAR = (
            "X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*"
    ).getBytes(StandardCharsets.US_ASCII);

    @Test
    void acceptsOnlyCompletedOkScan() throws Exception {
        withFakeClamd("stream: OK\0", port -> assertDoesNotThrow(
                () -> service(port).scan(new ByteArrayInputStream("safe".getBytes()))
        ));
    }

    @Test
    void rejectsEicarWhenClamdReportsFound() throws Exception {
        withFakeClamd("stream: Eicar-Signature FOUND\0", port -> assertThrows(
                InfectedFileException.class,
                () -> service(port).scan(new ByteArrayInputStream(EICAR))
        ));
    }

    @Test
    void rejectsClamdStreamLimit() throws Exception {
        withFakeClamd("INSTREAM size limit exceeded. ERROR\0", port -> assertThrows(
                FileTooLargeException.class,
                () -> service(port).scan(new ByteArrayInputStream(EICAR))
        ));
    }

    @Test
    void rejectsUnavailableClamd() throws IOException {
        int unusedPort;
        try (ServerSocket socket = new ServerSocket(0)) {
            unusedPort = socket.getLocalPort();
        }
        ClamAvServiceImpl service = service(unusedPort, 100);

        assertThrows(
                AntivirusUnavailableException.class,
                () -> service.scan(new ByteArrayInputStream(EICAR))
        );
    }

    @Test
    void rejectsTimedOutScan() throws Exception {
        try (ServerSocket server = new ServerSocket(0)) {
            CompletableFuture<Void> daemon = CompletableFuture.runAsync(() -> respondAfterDelay(server));
            ClamAvServiceImpl service = service(server.getLocalPort(), 50);

            assertThrows(
                    AntivirusUnavailableException.class,
                    () -> service.scan(new ByteArrayInputStream(EICAR))
            );
            daemon.get(3, TimeUnit.SECONDS);
        }
    }

    private ClamAvServiceImpl service(int port) {
        return service(port, 2000);
    }

    private ClamAvServiceImpl service(int port, int timeout) {
        return new ClamAvServiceImpl(new ClamAvProperties("127.0.0.1", port, timeout));
    }

    private void withFakeClamd(String response, PortAssertion assertion) throws Exception {
        try (ServerSocket server = new ServerSocket(0)) {
            CompletableFuture<Void> daemon = CompletableFuture.runAsync(() -> respond(server, response));
            assertion.run(server.getLocalPort());
            daemon.get(3, TimeUnit.SECONDS);
        }
    }

    private void respond(ServerSocket server, String response) {
        try (Socket socket = server.accept()) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            readNullTerminated(input);
            int size;
            while ((size = input.readInt()) > 0) {
                input.readNBytes(size);
            }
            socket.getOutputStream().write(response.getBytes(StandardCharsets.US_ASCII));
            socket.getOutputStream().flush();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void respondAfterDelay(ServerSocket server) {
        try (Socket socket = server.accept()) {
            Thread.sleep(150);
            socket.getOutputStream().write("stream: OK\0".getBytes(StandardCharsets.US_ASCII));
        } catch (IOException exception) {
            // The client is expected to close the timed-out connection.
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(exception);
        }
    }

    private void readNullTerminated(DataInputStream input) throws IOException {
        while (input.readByte() != 0) {
            // Read the framed command.
        }
    }

    @FunctionalInterface
    private interface PortAssertion {
        void run(int port) throws Exception;
    }
}
