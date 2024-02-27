package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectStampRequest {

    private String studentName;

    private String department;

    @Size(min = 8, max = 8, message = "학번은 8자리입니다.")
    private String studentNumber;

    private String clubCode;

    public StampHistory toStampHistoryEntity() {
        return StampHistory.builder()
                .studentName(this.studentName)
                .department(this.department)
                .studentNumber(this.studentNumber).build();
    }
}
