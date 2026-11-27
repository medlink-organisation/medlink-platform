package medlink.project.schedulingservice.conifg;

import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class PraticienGrpcClientConfig {

    @Bean
    AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub authGrpcStub(
            GrpcChannelFactory channelFactory
    ) {
        return AuthGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel("auth-service")
        );
    }

    @Bean
    PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienGrpcStub(
            GrpcChannelFactory channelFactory
    ) {
        return PraticienGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel("praticien-service")
        );
    }
}
