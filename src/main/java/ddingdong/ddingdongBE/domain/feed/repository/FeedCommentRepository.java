package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    List<FeedComment> findAllByFeedIdOrderByCreatedAtAsc(Long feedId);

    long countByFeedId(Long feedId);
}
