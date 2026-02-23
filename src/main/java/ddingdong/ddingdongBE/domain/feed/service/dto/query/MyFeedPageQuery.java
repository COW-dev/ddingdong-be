package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.repository.dto.MyFeedStatDto;
import java.util.Collections;
import java.util.List;
import lombok.Builder;

@Builder
public record MyFeedPageQuery(
    long feedCount,
    long totalViewCount,
    long imageCount,
    long videoCount,
    List<FeedListQuery> feedListQueries,
    PagingQuery pagingQuery
) {

    public static MyFeedPageQuery of(MyFeedStatDto stat, List<FeedListQuery> feedListQueries,
            PagingQuery pagingQuery) {
        return MyFeedPageQuery.builder()
                .feedCount(stat.getFeedCount())
                .totalViewCount(stat.getTotalViewCount())
                .imageCount(stat.getImageCount())
                .videoCount(stat.getVideoCount())
                .feedListQueries(feedListQueries)
                .pagingQuery(pagingQuery)
                .build();
    }

    public static MyFeedPageQuery createEmpty() {
        return MyFeedPageQuery.builder()
                .feedCount(0)
                .totalViewCount(0)
                .imageCount(0)
                .videoCount(0)
                .feedListQueries(Collections.emptyList())
                .pagingQuery(PagingQuery.createEmpty())
                .build();
    }
}
