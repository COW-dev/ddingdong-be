package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedLike;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    boolean existsByFeedIdAndUuid(Long feedId, String uuid);

    void deleteByFeedIdAndUuid(Long feedId, String uuid);

    long countByFeedId(Long feedId);

    @Query(value = """
            SELECT fl.feed_id AS feedId, COUNT(*) AS cnt
            FROM feed_like fl
            WHERE fl.feed_id IN (:feedIds)
            GROUP BY fl.feed_id
            """, nativeQuery = true)
    List<FeedCountDto> countsByFeedIds(@Param("feedIds") List<Long> feedIds);
}
