package ddingdong.ddingdongBE.domain.feed.service;

public interface FeedLikeService {

    void create(Long feedId, Long userId);

    long countByFeedId(Long feedId);

    boolean existsByFeedIdAndUserId(Long feedId, Long userId);
}
