package medlink.project.notificationservice.messaging;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.entity.NotificationType;
import medlink.project.notificationservice.entity.RecipientType;
import medlink.project.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationEventConsumerTest {

    @Test
    void delegatesKafkaEventToNotificationService() {
        NotificationService service = mock(NotificationService.class);
        Consumer<CreateNotificationRequest> consumer =
                new NotificationEventConsumer().notificationEvents(service);
        CreateNotificationRequest event = new CreateNotificationRequest(
                "patient-sub", RecipientType.PATIENT,
                NotificationType.RENDEZ_VOUS_CONFIRMATION,
                "Confirmation", "Message", "patient@example.com", true, "wf-1"
        );

        consumer.accept(event);

        verify(service).create(event);
    }
}
