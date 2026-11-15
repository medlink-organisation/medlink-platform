package medlink.project.authservice.confi;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import medlink.grpc.praticien.v1.PraticienGrpcServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class PraticienGrpcClientConfig {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel praticienChannel(
            @Value("${medlink.grpc.praticien.host}")
            String host,

            @Value("${medlink.grpc.praticien.port}")
            int port
    ) {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public PraticienGrpcServiceGrpc.PraticienGrpcServiceBlockingStub
    praticienStub(ManagedChannel praticienChannel) {

        return PraticienGrpcServiceGrpc
                .newBlockingStub(praticienChannel);
    }
}