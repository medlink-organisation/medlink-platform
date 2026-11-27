package medlink.project.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
    @Bean
    RouteLocator adminAuditRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("praticien-audit", route -> route
                        .path("/api/admin/audit/praticien", "/api/admin/audit/praticien/**")
                        .filters(filters -> filters.rewritePath(
                                "/api/admin/audit/praticien(?<segment>/?.*)",
                                "/api/admin/audit${segment}"
                        ))
                        .uri("lb://praticien-service"))
                .route("directory-audit", route -> route
                        .path("/api/admin/audit/directory", "/api/admin/audit/directory/**")
                        .filters(filters -> filters.rewritePath(
                                "/api/admin/audit/directory(?<segment>/?.*)",
                                "/api/admin/audit${segment}"
                        ))
                        .uri("lb://directory-service"))
                .route("medical-record-audit", route -> route
                        .path("/api/admin/audit/medical-record", "/api/admin/audit/medical-record/**")
                        .filters(filters -> filters.rewritePath(
                                "/api/admin/audit/medical-record(?<segment>/?.*)",
                                "/api/admin/audit${segment}"
                        ))
                        .uri("lb://medical-record-service"))
                .route("scheduling-audit", route -> route
                        .path("/api/admin/audit/scheduling", "/api/admin/audit/scheduling/**")
                        .filters(filters -> filters.rewritePath(
                                "/api/admin/audit/scheduling(?<segment>/?.*)",
                                "/api/admin/audit${segment}"
                        ))
                        .uri("lb://scheduling-service"))
                .build();
    }
}

