package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DetailAppliedUserResponse {

    private Long id;
    private String studentName;
    private String studentNumber;
    private String department;
    private List<CollectedStampsResponse> collections;
    private String certificationImageUrl;

    public static DetailAppliedUserResponse from(StampHistory stampHistory) {
        return DetailAppliedUserResponse.builder()
                .id(stampHistory.getId())
                .studentName(stampHistory.getStudentName())
                .studentNumber(stampHistory.getStudentNumber())
                .department(stampHistory.getDepartment())
                .collections(stampHistory.getCollectedStamps().keySet().stream()
                        .map(stamp -> CollectedStampsResponse.of(stamp.getName(),
                                stampHistory.getCollectedStamps().get(stamp)))
                        .toList())
                .certificationImageUrl(stampHistory.getCertificationImageUrl()).build();
    }

}
