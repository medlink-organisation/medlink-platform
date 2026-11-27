package medlink.project.notificationservice.messaging;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class NotificationEventConsumer {

    @Bean
    Consumer<CreateNotificationRequest> notificationEvents(NotificationService notificationService) {
        return notificationService::create;
    }
}
