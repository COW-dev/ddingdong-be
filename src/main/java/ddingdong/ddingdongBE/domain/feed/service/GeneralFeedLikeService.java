package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.FeedException.DuplicatedFeedLikeException;
import ddingdong.ddingdongBE.common.exception.FeedException.FeedLikeNotFoundException;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedLike;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedLikeService implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedService feedService;
    private final FeedLikeCacheService feedLikeCacheService;

    @Override
    @Transactional
    public void create(Long feedId, String uuid) {
        if (feedLikeCacheService.isLiked(uuid, feedId)) {
            throw new DuplicatedFeedLikeException();
        }
        if (feedLikeRepository.existsByFeedIdAndUuid(feedId, uuid)) {
            throw new DuplicatedFeedLikeException();
        }
        Feed feed = feedService.getById(feedId);
        FeedLike feedLike = FeedLike.builder()
                .feed(feed)
                .uuid(uuid)
                .build();
        feedLikeRepository.save(feedLike);
        feedLikeCacheService.addLike(uuid, feedId);
    }

    @Override
    @Transactional
    public void delete(Long feedId, String uuid) {
        if (!feedLikeCacheService.isLiked(uuid, feedId)
                && !feedLikeRepository.existsByFeedIdAndUuid(feedId, uuid)) {
            throw new FeedLikeNotFoundException();
        }
        feedLikeRepository.deleteByFeedIdAndUuid(feedId, uuid);
        feedLikeCacheService.removeLike(uuid, feedId);
    }

    @Override
    public long countByFeedId(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    @Override
    public boolean existsByFeedIdAndUuid(Long feedId, String uuid) {
        return feedLikeCacheService.isLiked(uuid, feedId)
                || feedLikeRepository.existsByFeedIdAndUuid(feedId, uuid);
    }
}
