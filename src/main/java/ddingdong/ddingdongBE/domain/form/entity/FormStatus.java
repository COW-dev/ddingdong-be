package ddingdong.ddingdongBE.domain.form.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FormStatus {
    UPCOMING("진행 전"),
    ONGOING("진행 중"),
    CLOSED("마감");

    private final String description;
}
