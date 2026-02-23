package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubProfileQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedFileInfoQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import java.util.Collections;
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
    private final FeedLikeService feedLikeService;
    private final FeedCommentService feedCommentService;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentRepository feedCommentRepository;

    public ClubFeedPageQuery getFeedPageByClub(Long clubId, int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getFeedPageByClubId(clubId, size, currentCursorId);
        if (feedPage == null) {
            return ClubFeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = buildFeedListQueriesWithCounts(completeFeeds);
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return ClubFeedPageQuery.of(feedListQueries, pagingQuery);
    }

    public FeedPageQuery getAllFeedPage(int size, Long currentCursorId) {
        Slice<Feed> feedPage = feedService.getAllFeedPage(size, currentCursorId);
        if (feedPage == null) {
            return FeedPageQuery.createEmpty();
        }
        List<Feed> completeFeeds = feedPage.getContent();
        List<FeedListQuery> feedListQueries = buildFeedListQueriesWithCounts(completeFeeds);
        PagingQuery pagingQuery = PagingQuery.of(currentCursorId, completeFeeds, feedPage.hasNext());

        return FeedPageQuery.of(feedListQueries, pagingQuery);
    }

    @Transactional
    public FeedQuery getById(Long feedId) {
        feedService.incrementViewCount(feedId);
        Feed feed = feedService.getById(feedId);
        ClubProfileQuery clubProfileQuery = feedFileService.extractClubInfo(feed.getClub());
        FeedFileInfoQuery feedFileInfoQuery = feedFileService.extractFeedFileInfo(feed);
        long likeCount = feedLikeService.countByFeedId(feedId);
        long commentCount = feedCommentService.countByFeedId(feedId);
        List<FeedCommentQuery> comments = feedCommentService.getAllByFeedId(feedId);
        return FeedQuery.of(feed, clubProfileQuery, feedFileInfoQuery, likeCount, commentCount, comments);
    }

    private List<FeedListQuery> buildFeedListQueriesWithCounts(List<Feed> feeds) {
        List<FeedListQuery> feedListQueries = feeds.stream()
                .map(feedFileService::extractFeedThumbnailInfo)
                .toList();

        List<Long> feedIds = feeds.stream().map(Feed::getId).toList();
        if (feedIds.isEmpty()) {
            return feedListQueries;
        }

        Map<Long, Long> likeCountMap = feedLikeRepository.countsByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(FeedCountDto::getFeedId, FeedCountDto::getCnt));
        Map<Long, Long> commentCountMap = feedCommentRepository.countsByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(FeedCountDto::getFeedId, FeedCountDto::getCnt));

        return feedListQueries.stream()
                .map(q -> FeedListQuery.builder()
                        .id(q.id())
                        .thumbnailCdnUrl(q.thumbnailCdnUrl())
                        .thumbnailOriginUrl(q.thumbnailOriginUrl())
                        .feedType(q.feedType())
                        .thumbnailFileName(q.thumbnailFileName())
                        .viewCount(q.viewCount())
                        .likeCount(likeCountMap.getOrDefault(q.id(), 0L))
                        .commentCount(commentCountMap.getOrDefault(q.id(), 0L))
                        .build())
                .toList();
    }

}
