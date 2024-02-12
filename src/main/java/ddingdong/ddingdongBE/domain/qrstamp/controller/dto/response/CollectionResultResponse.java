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
public class CollectionResultResponse {

    private boolean isCompleted;
    private List<CollectedStampsResponse> collections;

    public static CollectionResultResponse of(boolean isCompleted,
                                              List<CollectedStampsResponse> collectedStampsResponse) {
        return CollectionResultResponse.builder()
                .isCompleted(isCompleted)
                .collections(collectedStampsResponse)
                .build();
    }
}
