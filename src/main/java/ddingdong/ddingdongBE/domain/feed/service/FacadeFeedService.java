package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileInfoQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final FeedCommentService feedCommentService;

    public ClubFeedPageQuery getFeedPageByClub(Long clubId, int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getFeedPageByClubId(clubId, size, currentCursorId);
        if (feedPage == null) {
            return ClubFeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = buildFeedListQueries(completeFeeds);
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return ClubFeedPageQuery.of(feedListQueries, pagingQuery);
    }

    public FeedPageQuery getAllFeedPage(int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getAllFeedPage(size, currentCursorId);
        if (feedPage == null) {
            return FeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = buildFeedListQueries(completeFeeds);
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return FeedPageQuery.of(feedListQueries, pagingQuery);
    }

    private List<FeedListQuery> buildFeedListQueries(List<Feed> feeds) {
        if (feeds.isEmpty()) {
            return List.of();
        }
        List<Long> feedIds = feeds.stream().map(Feed::getId).toList();
        Map<Long, Long> commentCountMap = feedCommentService.countsByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(FeedCountDto::getFeedId, FeedCountDto::getCnt));

        return feeds.stream()
                .map(feed -> FeedListQuery.of(
                        feed,
                        feedFileService.extractFeedThumbnailInfo(feed),
                        commentCountMap.getOrDefault(feed.getId(), 0L)))
                .toList();
    }

    @Transactional
    public FeedQuery getById(Long feedId) {
        feedService.incrementViewCount(feedId);
        Feed feed = feedService.getById(feedId);
        ClubProfileQuery clubProfileQuery = feedFileService.extractClubInfo(feed.getClub());
        FeedFileInfoQuery feedFileInfoQuery = feedFileService.extractFeedFileInfo(feed);
        long commentCount = feedCommentService.countByFeedId(feedId);
        List<FeedCommentQuery> comments = feedCommentService.getAllByFeedId(feedId);
        return FeedQuery.of(feed, clubProfileQuery, feedFileInfoQuery, commentCount, comments);
    }

}
