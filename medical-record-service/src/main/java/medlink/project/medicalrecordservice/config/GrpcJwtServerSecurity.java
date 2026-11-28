package medlink.project.medicalrecordservice.config;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import medlink.project.medicalrecordservice.security.JwtAuthConverter;

@Configuration
public class GrpcJwtServerSecurity {

    private static final Metadata.Key<String> AUTHORIZATION = Metadata.Key.of(
            "authorization", Metadata.ASCII_STRING_MARSHALLER);
    private static final Context.Key<Authentication> AUTHENTICATION =
            Context.key("medilink-authentication");

    @Bean
    @GlobalServerInterceptor
    ServerInterceptor grpcJwtAuthenticationInterceptor(
            JwtDecoder jwtDecoder,
            JwtAuthConverter jwtAuthConverter
    ) {
        return new ServerInterceptor() {
            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                    ServerCall<ReqT, RespT> call,
                    Metadata headers,
                    ServerCallHandler<ReqT, RespT> next
            ) {
                try {
                    String authorization = headers.get(AUTHORIZATION);
                    if (authorization == null || !authorization.startsWith("Bearer ")) {
                        return reject(call, Status.UNAUTHENTICATED
                                .withDescription("JWT utilisateur obligatoire."));
                    }
                    Jwt jwt = jwtDecoder.decode(authorization.substring(7));
                    Authentication authentication = jwtAuthConverter.convert(jwt);
                    if (authentication == null || !authentication.isAuthenticated()) {
                        return reject(call, Status.UNAUTHENTICATED
                                .withDescription("JWT utilisateur invalide."));
                    }
                    String method = call.getMethodDescriptor().getBareMethodName();
                    String requiredRole = "RegularizeCompletedAppointment".equals(method)
                            ? "ADMIN" : "PRATICIEN";
                    if (!hasRole(authentication, requiredRole)) {
                        return reject(call, Status.PERMISSION_DENIED
                                .withDescription("Rôle utilisateur insuffisant."));
                    }
                    Context context = Context.current().withValue(AUTHENTICATION, authentication);
                    ServerCall.Listener<ReqT> listener = Contexts.interceptCall(
                            context, call, headers, next);
                    return withSecurityContext(listener, authentication);
                } catch (Exception exception) {
                    return reject(call, Status.UNAUTHENTICATED
                            .withDescription("JWT utilisateur invalide."));
                }
            }
        };
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(role)
                        || authority.equals("ROLE_" + role));
    }

    private <ReqT, RespT> ServerCall.Listener<ReqT> reject(
            ServerCall<ReqT, RespT> call,
            Status status
    ) {
        call.close(status, new Metadata());
        return new ServerCall.Listener<>() {};
    }

    private <ReqT> ServerCall.Listener<ReqT> withSecurityContext(
            ServerCall.Listener<ReqT> delegate,
            Authentication authentication
    ) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                authenticated(authentication, () -> super.onMessage(message));
            }

            @Override
            public void onHalfClose() {
                authenticated(authentication, super::onHalfClose);
            }

            @Override
            public void onCancel() {
                authenticated(authentication, super::onCancel);
            }

            @Override
            public void onComplete() {
                authenticated(authentication, super::onComplete);
            }

            @Override
            public void onReady() {
                authenticated(authentication, super::onReady);
            }
        };
    }

    private void authenticated(Authentication authentication, Runnable action) {
        SecurityContext previous = SecurityContextHolder.getContext();
        SecurityContext current = SecurityContextHolder.createEmptyContext();
        current.setAuthentication(authentication);
        SecurityContextHolder.setContext(current);
        try {
            action.run();
        } finally {
            SecurityContextHolder.setContext(previous);
        }
    }
}
