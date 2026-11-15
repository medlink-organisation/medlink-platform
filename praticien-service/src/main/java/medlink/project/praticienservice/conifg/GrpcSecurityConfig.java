package medlink.project.praticienservice.conifg;

import io.grpc.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class GrpcSecurityConfig {

    private static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of(
                    "authorization",
                    Metadata.ASCII_STRING_MARSHALLER
            );

    @Bean
    public ClientInterceptor grpcSecurityInterceptor() {

        return new ClientInterceptor() {

            @Override
            public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
                    MethodDescriptor<ReqT, RespT> method,
                    CallOptions callOptions,
                    Channel next
            ) {

                var authentication = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

                return new ForwardingClientCall.SimpleForwardingClientCall<>(
                        next.newCall(method, callOptions)
                ) {

                    @Override
                    public void start(
                            Listener<RespT> responseListener,
                            Metadata headers
                    ) {

                        if (authentication instanceof JwtAuthenticationToken jwt) {

                            headers.put(
                                    AUTHORIZATION,
                                    "Bearer " + jwt.getToken().getTokenValue()
                            );
                        }

                        super.start(responseListener, headers);
                    }
                };
            }
        };
    }
}