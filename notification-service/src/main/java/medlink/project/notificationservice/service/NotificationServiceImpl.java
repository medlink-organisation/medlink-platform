package medlink.project.notificationservice.service;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.dto.NotificationDto;
import medlink.project.notificationservice.dto.SendEmailRequest;
import medlink.project.notificationservice.entity.Notification;
import medlink.project.notificationservice.entity.RecipientType;
import medlink.project.notificationservice.exception.NotificationDeliveryException;
import medlink.project.notificationservice.mapper.NotificationMapper;
import medlink.project.notificationservice.repository.NotificationRepository;
import medlink.project.notificationservice.validation.NotificationValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final NotificationValidator validator;
    private final EmailService emailService;

    public NotificationServiceImpl(
            NotificationRepository repository,
            NotificationMapper mapper,
            NotificationValidator validator,
            EmailService emailService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public NotificationDto create(CreateNotificationRequest request) {
        validator.validateCreate(request);
        Notification notification = mapper.toEntity(request);
        if (request.sendEmail()) {
            try {
                emailService.send(new SendEmailRequest(
                        request.email(), request.title(), request.message(), request.correlationId()
                ));
                notification.setEmailSent(true);
            } catch (NotificationDeliveryException ignored) {
                notification.setEmailSent(false);
            }
        }
        return mapper.toDto(repository.save(notification));
    }

    @Override
    public Page<NotificationDto> getForUser(
            String recipientUserId,
            Set<RecipientType> recipientTypes,
            boolean unreadOnly,
            Pageable pageable
    ) {
        Page<Notification> notifications = unreadOnly
                ? repository.findAllByRecipientUserIdAndRecipientTypeInAndReadFalse(
                        recipientUserId, recipientTypes, pageable
                )
                : repository.findAllByRecipientUserIdAndRecipientTypeIn(
                        recipientUserId, recipientTypes, pageable
                );
        return notifications.map(mapper::toDto);
    }

    @Override
    public long countUnread(String recipientUserId, Set<RecipientType> recipientTypes) {
        return repository.countByRecipientUserIdAndRecipientTypeInAndReadFalse(
                recipientUserId, recipientTypes
        );
    }

    @Override
    @Transactional
    public NotificationDto markAsRead(
            UUID notificationId,
            String recipientUserId,
            Set<RecipientType> recipientTypes
    ) {
        Notification notification = validator.requireOwned(
                notificationId, recipientUserId, recipientTypes
        );
        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(Instant.now());
        }
        return mapper.toDto(repository.save(notification));
    }

    @Override
    @Transactional
    public int markAllAsRead(String recipientUserId, Set<RecipientType> recipientTypes) {
        return repository.markAllAsRead(recipientUserId, recipientTypes, Instant.now());
    }
}
