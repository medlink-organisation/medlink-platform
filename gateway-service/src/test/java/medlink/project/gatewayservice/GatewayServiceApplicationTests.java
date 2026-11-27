package medlink.project.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GatewayServiceApplicationTests {

    private final RouteLocator adminAuditRoutes;

    GatewayServiceApplicationTests(@Qualifier("adminAuditRoutes") RouteLocator adminAuditRoutes) {
        this.adminAuditRoutes = adminAuditRoutes;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void exposesEveryAdminAuditRoute() {
        List<String> routeIds = adminAuditRoutes.getRoutes()
                .map(route -> route.getId())
                .collectList()
                .block();

        assertEquals(
                List.of("praticien-audit", "directory-audit", "medical-record-audit", "scheduling-audit"),
                routeIds
        );
    }

    @TestConfiguration
    static class DiscoveryTestConfiguration {

        @Bean
        @Primary
        ReactiveDiscoveryClient reactiveDiscoveryClient() {
            return new ReactiveDiscoveryClient() {
                @Override
                public String description() {
                    return "test";
                }

                @Override
                public Flux<org.springframework.cloud.client.ServiceInstance> getInstances(String serviceId) {
                    return Flux.empty();
                }

                @Override
                public Flux<String> getServices() {
                    return Flux.empty();
                }
            };
        }
    }

}
