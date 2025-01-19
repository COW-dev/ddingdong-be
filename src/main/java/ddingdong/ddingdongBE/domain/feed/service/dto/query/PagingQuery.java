package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;

public record PagingQuery(
    Long currentCursorId,
    Long nextCursorId,
    boolean hasNext
) {

    public static PagingQuery of(Long currentCursorId, List<Feed> completeFeeds, boolean hasNext) {
        if (completeFeeds.isEmpty()) {
            return new PagingQuery(currentCursorId, currentCursorId, false);
        }
        return new PagingQuery(currentCursorId, completeFeeds.get(completeFeeds.size() - 1).getId(), hasNext);
    }
}
