package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    @Query("SELECT fc.anonymousNumber FROM FeedComment fc WHERE fc.feed.id = :feedId AND fc.uuid = :uuid ORDER BY fc.id ASC")
    Optional<Integer> findAnonymousNumberByFeedIdAndUuid(
            @Param("feedId") Long feedId,
            @Param("uuid") String uuid
    );

    @Query("SELECT COALESCE(MAX(fc.anonymousNumber), 0) FROM FeedComment fc WHERE fc.feed.id = :feedId")
    int findMaxAnonymousNumberByFeedId(@Param("feedId") Long feedId);

    List<FeedComment> findAllByFeedIdOrderByCreatedAtAsc(Long feedId);

    long countByFeedId(Long feedId);
}
