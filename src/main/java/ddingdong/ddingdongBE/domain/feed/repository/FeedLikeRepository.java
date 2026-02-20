package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    boolean existsByFeedIdAndUserId(Long feedId, Long userId);

    void deleteByFeedIdAndUserId(Long feedId, Long userId);

    long countByFeedId(Long feedId);
}
