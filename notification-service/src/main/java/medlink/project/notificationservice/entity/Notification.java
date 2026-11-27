package medlink.project.notificationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.envers.Audited;

import java.time.Instant;
import java.util.UUID;

@Entity
@Audited
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notification_recipient", columnList = "recipient_user_id,created_at"),
                @Index(name = "idx_notification_unread", columnList = "recipient_user_id,is_read")
        }
)
public class Notification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "recipient_user_id", nullable = false, length = 255)
    private String recipientUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false, length = 24)
    private RecipientType recipientType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 48)
    private NotificationType notificationType;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 5000)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "email_recipient", length = 320)
    private String emailRecipient;

    @Column(name = "email_sent", nullable = false)
    private boolean emailSent;

    @Version
    private long version;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(String recipientUserId) { this.recipientUserId = recipientUserId; }
    public RecipientType getRecipientType() { return recipientType; }
    public void setRecipientType(RecipientType recipientType) { this.recipientType = recipientType; }
    public NotificationType getNotificationType() { return notificationType; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public Instant getReadAt() { return readAt; }
    public void setReadAt(Instant readAt) { this.readAt = readAt; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getEmailRecipient() { return emailRecipient; }
    public void setEmailRecipient(String emailRecipient) { this.emailRecipient = emailRecipient; }
    public boolean isEmailSent() { return emailSent; }
    public void setEmailSent(boolean emailSent) { this.emailSent = emailSent; }
    public long getVersion() { return version; }
}
