package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import java.util.List;

public record MyFeedPageQuery(
    List<FeedListQuery> feedListQueries,
    PagingQuery pagingQuery
) {

    public static MyFeedPageQuery of(List<FeedListQuery> feedListQueries, PagingQuery pagingQuery) {
        return new MyFeedPageQuery(feedListQueries, pagingQuery);
    }
}
