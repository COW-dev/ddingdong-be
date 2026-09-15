package ddingdong.ddingdongBE.domain.activityreport.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityReportTermStatus {

    UPCOMING("진행 전"),
    ONGOING("진행 중"),
    CLOSED("진행 종료");

    private final String text;
}