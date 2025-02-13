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


    public static FormStatus getDescription(LocalDate now, LocalDate startDate, LocalDate endDate) {
        if (now.isBefore(startDate)) {
            return FormStatus.UPCOMING;
        }

        if (!now.isAfter(endDate)) {
            return FormStatus.ONGOING;
        }

        return FormStatus.CLOSED;
    }
}
