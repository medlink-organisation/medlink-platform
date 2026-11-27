package medlink.project.notificationservice.service;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.dto.NotificationDto;
import medlink.project.notificationservice.entity.RecipientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.Set;

public interface NotificationService {

    NotificationDto create(CreateNotificationRequest request);

    Page<NotificationDto> getForUser(
            String recipientUserId,
            Set<RecipientType> recipientTypes,
            boolean unreadOnly,
            Pageable pageable
    );

    long countUnread(String recipientUserId, Set<RecipientType> recipientTypes);

    NotificationDto markAsRead(
            UUID notificationId,
            String recipientUserId,
            Set<RecipientType> recipientTypes
    );

    int markAllAsRead(String recipientUserId, Set<RecipientType> recipientTypes);
}
