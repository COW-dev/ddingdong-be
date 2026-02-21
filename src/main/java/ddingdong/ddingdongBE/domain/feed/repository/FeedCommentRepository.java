package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    // soft delete 여부와 관계없이 UUID 기존 번호 조회 (native query)
    @Query(value = "SELECT fc.anonymous_number FROM feed_comment fc WHERE fc.feed_id = :feedId AND fc.uuid = :uuid ORDER BY fc.id ASC LIMIT 1",
            nativeQuery = true)
    Optional<Integer> findAnonymousNumberByFeedIdAndUuid(
            @Param("feedId") Long feedId,
            @Param("uuid") String uuid
    );

    // soft delete 여부와 관계없이 최대 번호 조회 + 비관적 락 (동시성 보호)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT COALESCE(MAX(fc.anonymous_number), 0) FROM feed_comment fc WHERE fc.feed_id = :feedId",
            nativeQuery = true)
    int findMaxAnonymousNumberByFeedId(@Param("feedId") Long feedId);

    List<FeedComment> findAllByFeedIdOrderByCreatedAtAsc(Long feedId);

    long countByFeedId(Long feedId);
}
