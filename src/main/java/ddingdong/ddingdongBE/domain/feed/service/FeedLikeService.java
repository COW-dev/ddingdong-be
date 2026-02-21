package ddingdong.ddingdongBE.domain.feed.service;

public interface FeedLikeService {

    void create(Long feedId, String uuid);

    void delete(Long feedId, String uuid);

    long countByFeedId(Long feedId);

    boolean existsByFeedIdAndUuid(Long feedId, String uuid);
}
