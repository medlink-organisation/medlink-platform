package medlink.project.authservice.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import medlink.grpc.auth.v1.GetUserContactRequest;
import medlink.grpc.auth.v1.GetPatientByIdRequest;
import medlink.grpc.auth.v1.PatientResponse;
import medlink.grpc.auth.v1.UserContactResponse;
import medlink.project.authservice.service.KeycloakUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthGrpcEndpointTest {

    private KeycloakUserService keycloakUserService;
    private AuthGrpcEndpoint endpoint;

    @BeforeEach
    void setUp() {
        keycloakUserService = mock(KeycloakUserService.class);
        endpoint = new AuthGrpcEndpoint(keycloakUserService);
    }

    @Test
    void returnsUserContact() {
        UserRepresentation user = new UserRepresentation();
        user.setId("kc-123");
        user.setEmail("patient@medlink.tn");
        user.setFirstName("Amina");
        user.setLastName("Ben Salah");
        user.setEnabled(true);
        when(keycloakUserService.getUser("kc-123")).thenReturn(user);

        TestObserver<UserContactResponse> observer = new TestObserver<>();
        endpoint.getUserContact(request("kc-123"), observer);

        assertNull(observer.error);
        assertEquals("kc-123", observer.response.getKeycloakId());
        assertEquals("patient@medlink.tn", observer.response.getEmail());
        assertEquals("Amina Ben Salah", observer.response.getDisplayName());
        assertEquals(true, observer.response.getEnabled());
        assertEquals(true, observer.completed);
    }

    @Test
    void rejectsBlankKeycloakId() {
        TestObserver<UserContactResponse> observer = new TestObserver<>();

        endpoint.getUserContact(request("  "), observer);

        assertEquals(Status.Code.INVALID_ARGUMENT, statusCode(observer.error));
        verify(keycloakUserService, never()).getUser(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void returnsNotFoundWhenKeycloakUserDoesNotExist() {
        when(keycloakUserService.getUser("missing"))
                .thenThrow(new NotFoundException());
        TestObserver<UserContactResponse> observer = new TestObserver<>();

        endpoint.getUserContact(request("missing"), observer);

        assertEquals(Status.Code.NOT_FOUND, statusCode(observer.error));
    }

    @Test
    void returnsPatientById() {
        var patient = new medlink.project.authservice.dto.PatientResponse(
                "kc-123",
                "patient@medlink.tn",
                "patient@medlink.tn",
                "Amina",
                "Ben Salah",
                true,
                true,
                0L
        );
        when(keycloakUserService.getPatientById("kc-123")).thenReturn(patient);
        TestObserver<PatientResponse> observer = new TestObserver<>();

        endpoint.getPatientById(
                GetPatientByIdRequest.newBuilder().setKeycloakId("kc-123").build(),
                observer
        );

        assertNull(observer.error);
        assertEquals("kc-123", observer.response.getId());
        assertEquals("Amina", observer.response.getFirstName());
        assertEquals(true, observer.completed);
    }

    @Test
    void resolvesActivePatientWithBusinessId() {
        var patient = new medlink.project.authservice.dto.PatientResponse(
                "kc-123",
                "patient@medlink.tn",
                "patient@medlink.tn",
                "Amina",
                "Ben Salah",
                true,
                true,
                42L
        );
        when(keycloakUserService.resolveActivePatient("kc-123")).thenReturn(patient);
        TestObserver<PatientResponse> observer = new TestObserver<>();

        endpoint.resolveActivePatient(
                GetPatientByIdRequest.newBuilder().setKeycloakId("kc-123").build(),
                observer
        );

        assertNull(observer.error);
        assertEquals("kc-123", observer.response.getId());
        assertEquals(42L, observer.response.getBusinessPatientId());
        assertEquals(true, observer.completed);
    }

    private GetUserContactRequest request(String keycloakId) {
        return GetUserContactRequest.newBuilder()
                .setKeycloakId(keycloakId)
                .build();
    }

    private Status.Code statusCode(Throwable error) {
        return ((StatusRuntimeException) error).getStatus().getCode();
    }

    private static final class TestObserver<T> implements StreamObserver<T> {

        private T response;
        private Throwable error;
        private boolean completed;

        @Override
        public void onNext(T response) {
            this.response = response;
        }

        @Override
        public void onError(Throwable error) {
            this.error = error;
        }

        @Override
        public void onCompleted() {
            completed = true;
        }
    }
}
