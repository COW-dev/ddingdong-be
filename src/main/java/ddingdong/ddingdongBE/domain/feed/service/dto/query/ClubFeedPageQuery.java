package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import java.util.Collections;
import java.util.List;

public record ClubFeedPageQuery(
    List<FeedListQuery> feedListQueries,
    PagingQuery pagingQuery
) {

    public static ClubFeedPageQuery of(List<FeedListQuery> feedListQueries, PagingQuery pagingQuery) {
        return new ClubFeedPageQuery(feedListQueries, pagingQuery);
    }

    public static ClubFeedPageQuery createEmpty() {
        return new ClubFeedPageQuery(Collections.emptyList(), PagingQuery.createEmpty());
    }
}
