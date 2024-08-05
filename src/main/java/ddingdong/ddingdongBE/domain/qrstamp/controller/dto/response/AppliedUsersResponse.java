package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class AppliedUsersResponse {

    private final Long id;
    private final String studentName;
    private final String studentNumber;
    private final String department;

    public static AppliedUsersResponse from(StampHistory stampHistory) {
        return AppliedUsersResponse.builder()
                .id(stampHistory.getId())
                .studentName(stampHistory.getStudentName())
                .studentNumber(stampHistory.getStudentNumber())
                .department(stampHistory.getDepartment()).build();
    }

}
