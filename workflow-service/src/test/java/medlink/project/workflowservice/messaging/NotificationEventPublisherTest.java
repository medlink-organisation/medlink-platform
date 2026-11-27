package medlink.project.workflowservice.messaging;

import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.exception.NotificationServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationEventPublisherTest {

    private final StreamBridge streamBridge = mock(StreamBridge.class);
    private final NotificationEventPublisher publisher =
            new NotificationEventPublisher(streamBridge);
    private final SendNotificationRequest event = new SendNotificationRequest(
            "patient-sub", "PATIENT", "RENDEZ_VOUS_CONFIRMATION",
            "Confirmation", "Message", "patient@example.com", true, "wf-1"
    );

    @Test
    void publishesNotificationOnConfiguredBinding() {
        when(streamBridge.send("notificationEvents-out-0", event)).thenReturn(true);

        publisher.publish(event);

        verify(streamBridge).send("notificationEvents-out-0", event);
    }

    @Test
    void failsWhenKafkaRejectsPublication() {
        when(streamBridge.send("notificationEvents-out-0", event)).thenReturn(false);

        assertThatThrownBy(() -> publisher.publish(event))
                .isInstanceOf(NotificationServiceException.class)
                .hasMessageContaining("Kafka");
    }
}
