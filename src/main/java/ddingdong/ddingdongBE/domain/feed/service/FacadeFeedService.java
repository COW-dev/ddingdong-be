package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileInfoQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeFeedService {

    private final FeedService feedService;
    private final FeedFileService feedFileService;

    public ClubFeedPageQuery getFeedPageByClub(Long clubId, int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getFeedPageByClubId(clubId, size, currentCursorId);
        if (feedPage == null) {
            return ClubFeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = completeFeeds.stream()
                .map(feedFileService::extractFeedThumbnailInfo)
                .toList();
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return ClubFeedPageQuery.of(feedListQueries, pagingQuery);
    }

    public FeedPageQuery getAllFeedPage(int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getAllFeedPage(size, currentCursorId);
        if (feedPage == null) {
            return FeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();

        List<FeedListQuery> feedListQueries = completeFeeds.stream().map(feedFileService::extractFeedThumbnailInfo)
                .toList();
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return FeedPageQuery.of(feedListQueries, pagingQuery);
    }

    @Transactional
    public FeedQuery getById(Long feedId) {
        Feed feed = feedService.getById(feedId);
        feedService.incrementViewCount(feedId);
        ClubProfileQuery clubProfileQuery = feedFileService.extractClubInfo(feed.getClub());
        FeedFileInfoQuery feedFileInfoQuery = feedFileService.extractFeedFileInfo(feed);
        return FeedQuery.of(feed, clubProfileQuery, feedFileInfoQuery);
    }

}
