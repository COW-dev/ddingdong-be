package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    boolean existsByFeedIdAndUuid(Long feedId, String uuid);

    void deleteByFeedIdAndUuid(Long feedId, String uuid);

    long countByFeedId(Long feedId);
}
