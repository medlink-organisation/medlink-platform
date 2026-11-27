package medlink.project.notificationservice.repository;

import medlink.project.notificationservice.entity.Notification;
import medlink.project.notificationservice.entity.RecipientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findAllByRecipientUserIdAndRecipientTypeIn(
            String recipientUserId, Set<RecipientType> recipientTypes, Pageable pageable
    );

    Page<Notification> findAllByRecipientUserIdAndRecipientTypeInAndReadFalse(
            String recipientUserId, Set<RecipientType> recipientTypes, Pageable pageable
    );

    Optional<Notification> findByIdAndRecipientUserIdAndRecipientTypeIn(
            UUID id, String recipientUserId, Set<RecipientType> recipientTypes
    );

    long countByRecipientUserIdAndRecipientTypeInAndReadFalse(
            String recipientUserId, Set<RecipientType> recipientTypes
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Notification notification
               set notification.read = true,
                   notification.readAt = :readAt
             where notification.recipientUserId = :recipientUserId
               and notification.recipientType in :recipientTypes
               and notification.read = false
            """)
    int markAllAsRead(
            @Param("recipientUserId") String recipientUserId,
            @Param("recipientTypes") Set<RecipientType> recipientTypes,
            @Param("readAt") Instant readAt
    );
}
