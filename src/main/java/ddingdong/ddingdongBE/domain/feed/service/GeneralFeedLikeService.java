package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.FeedException.DuplicatedFeedLikeException;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedLike;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedLikeService implements FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedService feedService;

    @Override
    @Transactional
    public void create(Long feedId, Long userId) {
        if (feedLikeRepository.existsByFeedIdAndUserId(feedId, userId)) {
            throw new DuplicatedFeedLikeException();
        }
        Feed feed = feedService.getById(feedId);
        FeedLike feedLike = FeedLike.builder()
                .feed(feed)
                .user(User.builder().id(userId).build())
                .build();
        feedLikeRepository.save(feedLike);
    }

    @Override
    public long countByFeedId(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    @Override
    public boolean existsByFeedIdAndUserId(Long feedId, Long userId) {
        return feedLikeRepository.existsByFeedIdAndUserId(feedId, userId);
    }
}
