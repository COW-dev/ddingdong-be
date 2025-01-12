package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import java.util.List;

public record NewestFeedPerClubPageQuery(
    List<FeedListQuery> feedListQueries,
    PagingQuery pagingQuery
) {

    public static NewestFeedPerClubPageQuery of(List<FeedListQuery> feedListQueries, PagingQuery pagingQuery) {
        return new NewestFeedPerClubPageQuery(feedListQueries, pagingQuery);
    }
}
