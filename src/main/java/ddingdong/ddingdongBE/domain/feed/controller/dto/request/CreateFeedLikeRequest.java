package ddingdong.ddingdongBE.domain.feed.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateFeedLikeRequest(
        @NotNull(message = "count는 null이 될 수 없습니다.")
        @Min(value = 1, message = "count는 1 이상이어야 합니다.")
        @Max(value = 100, message = "count는 100 이하여야 합니다.")
        Integer count
) {

}
