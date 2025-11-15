package ddingdong.ddingdongBE.email.entity;

import static ddingdong.ddingdongBE.email.entity.EmailSendStatus.PENDING;
import static ddingdong.ddingdongBE.email.entity.EmailSendStatus.PERMANENT_FAILURE;
import static ddingdong.ddingdongBE.email.entity.EmailSendStatus.SENDING;
import static ddingdong.ddingdongBE.email.entity.EmailSendStatus.TEMPORARY_FAILURE;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_application_id")
    private FormApplication formApplication;

    @Enumerated(EnumType.STRING)
    private EmailSendStatus status;

    private int retryCount;

    @Nullable
    @Column(name = "message_tracking_id")
    private String messageTrackingId;

    @Nullable
    private LocalDateTime sentAt;

    @Builder
    public EmailSendHistory(FormApplication formApplication, EmailSendStatus status,
            int retryCount, LocalDateTime sentAt) {
        this.formApplication = formApplication;
        this.status = status;
        this.retryCount = retryCount;
        this.sentAt = sentAt;
    }

    public EmailSendHistory(FormApplication formApplication, EmailSendStatus status) {
        this(formApplication, status, 0, null);
    }

    public static EmailSendHistory createPending(FormApplication formApplication) {
        return new EmailSendHistory(formApplication, PENDING);
    }

    public void trySend() {
        this.sentAt = LocalDateTime.now();
        if (this.status == SENDING) {
            retryCount++;
            return;
        }
        this.status = SENDING;
    }

    public void markRetryFail() {
        this.status = TEMPORARY_FAILURE;
    }

    public void markNonRetryFail() {
        this.status = PERMANENT_FAILURE;
    }

    public void updateMessageTrackingId(String messageId) {
        this.messageTrackingId = messageId;
    }
}
