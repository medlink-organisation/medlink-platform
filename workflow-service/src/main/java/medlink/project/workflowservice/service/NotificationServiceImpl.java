package medlink.project.workflowservice.service;

import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.messaging.NotificationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationEventPublisher eventPublisher;

    public NotificationServiceImpl(NotificationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void sendNotification(SendNotificationRequest request) {
        eventPublisher.publish(request);
    }
}
