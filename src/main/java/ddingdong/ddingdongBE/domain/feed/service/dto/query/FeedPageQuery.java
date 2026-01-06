package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import java.util.Collections;
import java.util.List;

public record FeedPageQuery(
    List<FeedListQuery> feedListQueries,
    PagingQuery pagingQuery
) {

    public static FeedPageQuery of(List<FeedListQuery> feedListQueries, PagingQuery pagingQuery) {
        return new FeedPageQuery(feedListQueries, pagingQuery);
    }

    public static FeedPageQuery createEmpty() {
        return new FeedPageQuery(Collections.emptyList(), PagingQuery.createEmpty());
    }
}
