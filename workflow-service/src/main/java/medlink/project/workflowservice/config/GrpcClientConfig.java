package medlink.project.workflowservice.config;

import medlink.grpc.auth.v1.AuthGrpcServiceGrpc;
import medlink.grpc.creaneau.v1.CreneauGrpcServiceGrpc;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import medlink.grpc.rendezvous.v1.RendezVousGrpcServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    private static final String SCHEDULING_SERVICE = "scheduling-service";
    private static final String AUTH_SERVICE = "auth-service";
    private static final String PRATICIEN_SERVICE = "praticien-service";

    @Bean
    CreneauGrpcServiceGrpc.CreneauGrpcServiceBlockingStub creneauGrpcStub(
            GrpcChannelFactory channelFactory) {

        return CreneauGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel(SCHEDULING_SERVICE)
        );
    }

    @Bean
    RendezVousGrpcServiceGrpc.RendezVousGrpcServiceBlockingStub rendezVousGrpcStub(
            GrpcChannelFactory channelFactory) {

        return RendezVousGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel(SCHEDULING_SERVICE)
        );
    }

    @Bean
    AuthGrpcServiceGrpc.AuthGrpcServiceBlockingStub authGrpcStub(
            GrpcChannelFactory channelFactory) {

        return AuthGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel(AUTH_SERVICE)
        );
    }

    @Bean
    PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub praticienGrpcStub(
            GrpcChannelFactory channelFactory) {

        return PraticienGrpcServiceGrpc.newBlockingStub(
                channelFactory.createChannel(PRATICIEN_SERVICE)
        );
    }
}
