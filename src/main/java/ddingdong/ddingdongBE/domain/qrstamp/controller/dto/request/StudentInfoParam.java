package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.request;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentInfoParam {

    private String studentName;

    @Size(min = 8, max = 8, message = "학번은 8자리입니다.")
    private String studentNumber;

    public StampHistory toStampHistoryEntity() {
        return StampHistory.builder()
                .studentName(this.studentName)
                .studentNumber(this.studentNumber).build();
    }
}
