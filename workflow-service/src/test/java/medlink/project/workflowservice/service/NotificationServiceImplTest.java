package medlink.project.workflowservice.service;

import medlink.project.workflowservice.dto.SendNotificationRequest;
import medlink.project.workflowservice.messaging.NotificationEventPublisher;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationServiceImplTest {

    @Test
    void delegatesNotificationToKafkaPublisher() {
        NotificationEventPublisher publisher = mock(NotificationEventPublisher.class);
        NotificationService service = new NotificationServiceImpl(publisher);
        SendNotificationRequest request = new SendNotificationRequest(
                "patient-sub", "PATIENT", "RENDEZ_VOUS_CONFIRMATION",
                "Confirmation", "Message", "patient@example.com", true, "wf-1"
        );

        service.sendNotification(request);

        verify(publisher).publish(request);
    }
}
