package medlink.project.workflowservice.service;

import medlink.project.workflowservice.dto.SendNotificationRequest;

public interface NotificationService {

    void sendNotification(SendNotificationRequest request);
}
