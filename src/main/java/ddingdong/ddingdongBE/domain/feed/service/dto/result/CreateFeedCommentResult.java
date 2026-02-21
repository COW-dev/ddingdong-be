package ddingdong.ddingdongBE.domain.feed.service.dto.result;

import lombok.Builder;

@Builder
public record CreateFeedCommentResult(Long commentId, int anonymousNumber) {
}
