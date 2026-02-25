package ddingdong.ddingdongBE.email.entity;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailSendStatus {

    PENDING("Pending"),
    SENDING("Sending"),
    TEMPORARY_FAILURE("Temporary Failure"),
    PERMANENT_FAILURE("Permanent Failure"),
    DELIVERY_SUCCESS("Delivery"),
    BOUNCE_REJECT("Bounce"),
    COMPLAINT_REJECT("Complaint"),
    ;

    private final String value;

    public static EmailSendStatus findByValue(String value) {
        return Arrays.stream(EmailSendStatus.values())
                .filter(emailSendStatus -> Objects.equals(emailSendStatus.value, value))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFound("해당 값(value = " + value + ")로 상태를 찾을 수 없습니다."));
    }

    public static List<EmailSendStatus> resendTargets() {
        return List.of(TEMPORARY_FAILURE, BOUNCE_REJECT);
    }

    public static List<EmailSendStatus> inFlightStatuses() {
        return List.of(PENDING, SENDING);
    }

    public boolean isSuccess() {
        return this == DELIVERY_SUCCESS;
    }

    public boolean isFail() {
        return this == TEMPORARY_FAILURE || this == PERMANENT_FAILURE ||
                this == BOUNCE_REJECT || this == COMPLAINT_REJECT;
    }

    public EmailSendUiStatus toUiStatus() {
        return switch (this) {
            case PENDING, SENDING -> EmailSendUiStatus.IN_PROGRESS;
            case DELIVERY_SUCCESS -> EmailSendUiStatus.SUCCESS;
            case TEMPORARY_FAILURE, BOUNCE_REJECT -> EmailSendUiStatus.TEMPORARY_FAILURE;
            case PERMANENT_FAILURE, COMPLAINT_REJECT -> EmailSendUiStatus.PERMANENT_FAILURE;
        };
    }
}
