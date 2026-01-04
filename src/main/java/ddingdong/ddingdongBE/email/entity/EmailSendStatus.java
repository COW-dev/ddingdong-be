package ddingdong.ddingdongBE.email.entity;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailSendStatus {

    PENDING("Pending"),
    SENDING("Sending"),
    TEMPORARY_FAILURE("Tempporary Failure"),
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
                .orElseThrow(() -> new ResourceNotFound("해당 값(value = " + value + ")로 상태를 찾을 수 없습니다."));
    }
}
