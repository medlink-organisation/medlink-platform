package medlink.project.workflowservice.messaging;

import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.exception.NotificationServiceException;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisher {

    private static final String OUTPUT_BINDING = "notificationEvents-out-0";

    private final StreamBridge streamBridge;

    public NotificationEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(SendNotificationRequest event) {
        if (!streamBridge.send(OUTPUT_BINDING, event)) {
            throw new NotificationServiceException(
                    "Impossible de publier la notification dans Kafka."
            );
        }
    }
}
