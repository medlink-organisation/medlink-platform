package medlink.project.notificationservice.service;

import medlink.project.auditcore.dto.AuditOperation;
import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.entity.NotificationType;
import medlink.project.notificationservice.entity.RecipientType;
import medlink.project.notificationservice.exception.NotificationNotFoundException;
import medlink.project.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@EnableTestBinder
class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private InputDestination inputDestination;

    @MockitoBean
    private JavaMailSender mailSender;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void consumesNotificationCommandFromKafka() {
        String json = """
                {
                  "recipientUserId": "patient-kafka",
                  "recipientType": "PATIENT",
                  "notificationType": "RENDEZ_VOUS_CONFIRMATION",
                  "title": "Confirmation Kafka",
                  "message": "Votre rendez-vous est confirmé.",
                  "email": null,
                  "sendEmail": false,
                  "correlationId": "workflow-kafka-1"
                }
                """;

        inputDestination.send(new GenericMessage<>(
                json.getBytes(StandardCharsets.UTF_8)
        ));

        assertThat(repository.findAll())
                .singleElement()
                .satisfies(notification -> {
                    assertThat(notification.getRecipientUserId()).isEqualTo("patient-kafka");
                    assertThat(notification.getCorrelationId()).isEqualTo("workflow-kafka-1");
                });
    }

    @Test
    void createsReadsAndAuditsAnApplicationNotification() {
        var created = notificationService.create(new CreateNotificationRequest(
                "patient-sub",
                RecipientType.PATIENT,
                NotificationType.RENDEZ_VOUS_CONFIRMATION,
                "Rendez-vous confirmé",
                "Votre rendez-vous a été confirmé.",
                null,
                false,
                "workflow-42"
        ));

        assertThat(created.read()).isFalse();
        assertThat(created.createdAt()).isNotNull();
        assertThat(notificationService.countUnread(
                "patient-sub", Set.of(RecipientType.PATIENT)
        )).isOne();
        assertThat(notificationService.getForUser(
                "patient-sub",
                Set.of(RecipientType.PATIENT),
                true,
                PageRequest.of(0, 20)
        )).hasSize(1);

        var read = notificationService.markAsRead(
                created.id(), "patient-sub", Set.of(RecipientType.PATIENT)
        );
        assertThat(read.read()).isTrue();
        assertThat(read.readAt()).isNotNull();
        assertThat(notificationService.countUnread(
                "patient-sub", Set.of(RecipientType.PATIENT)
        )).isZero();

        assertThat(auditService.getHistory(
                "notification", created.id().toString(), PageRequest.of(0, 20)
        ).getContent())
                .extracting(revision -> revision.operation())
                .contains(AuditOperation.ADD, AuditOperation.MOD);
    }

    @Test
    void preventsAnotherUserFromReadingOrUpdatingNotification() {
        var created = notificationService.create(new CreateNotificationRequest(
                "patient-owner",
                RecipientType.PATIENT,
                NotificationType.GENERALE,
                "Information",
                "Message privé.",
                null,
                false,
                null
        ));

        assertThat(notificationService.getForUser(
                "another-user",
                Set.of(RecipientType.PATIENT),
                false,
                PageRequest.of(0, 20)
        )).isEmpty();
        assertThatThrownBy(() ->
                notificationService.markAsRead(
                        created.id(), "another-user", Set.of(RecipientType.PATIENT)
                ))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    @Test
    void filtersNotificationsByAuthenticatedRecipientRole() {
        notificationService.create(new CreateNotificationRequest(
                "shared-sub",
                RecipientType.PATIENT,
                NotificationType.GENERALE,
                "Patient",
                "Notification patient.",
                null,
                false,
                null
        ));
        notificationService.create(new CreateNotificationRequest(
                "shared-sub",
                RecipientType.ADMIN,
                NotificationType.GENERALE,
                "Administration",
                "Notification administrateur.",
                null,
                false,
                null
        ));

        assertThat(notificationService.getForUser(
                "shared-sub",
                Set.of(RecipientType.PATIENT),
                false,
                PageRequest.of(0, 20)
        ).getContent())
                .extracting(notification -> notification.recipientType())
                .containsExactly(RecipientType.PATIENT);

        assertThat(notificationService.getForUser(
                "shared-sub",
                Set.of(RecipientType.ADMIN),
                false,
                PageRequest.of(0, 20)
        ).getContent())
                .extracting(notification -> notification.recipientType())
                .containsExactly(RecipientType.ADMIN);
    }
}
