package ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CollectedStampsResponse {

    private String stamp;
    private String collectedAt;

    public static CollectedStampsResponse of(String stampName, String collectedAt) {
        return CollectedStampsResponse.builder()
                .stamp(stampName)
                .collectedAt(collectedAt).build();
    }

}
