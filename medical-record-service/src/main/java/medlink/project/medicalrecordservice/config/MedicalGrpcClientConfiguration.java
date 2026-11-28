package medlink.project.medicalrecordservice.config;

import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class MedicalGrpcClientConfiguration {

    @Bean
    PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienStub(
            GrpcChannelFactory channelFactory
    ) {
        return PraticienGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel("praticien-service")
        );
    }

    @Bean
    RendezVousGrpcServiceGrpc.RendezVousGrpcServiceBlockingStub rendezVousStub(
            GrpcChannelFactory channelFactory
    ) {
        return RendezVousGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel("scheduling-service")
        );
    }

    @Bean
    AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub authStub(
            GrpcChannelFactory channelFactory
    ) {
        return AuthGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel("auth-service")
        );
    }
}
