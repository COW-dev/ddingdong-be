package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

  @Query(value = "SELECT * FROM feed "
      + "WHERE club_id = :clubId "
      + "AND deleted_at IS NULL "
      + "ORDER BY id DESC"
      , nativeQuery = true)
  List<Feed> findAllByClubIdOrderById(@Param("clubId") Long clubId);

  @Query(value = "SELECT * FROM feed f "
      + "WHERE f.id in "
      + "(SELECT max(id) "
      + "FROM feed "
      + "WHERE deleted_at IS NULL "
      + "GROUP BY club_id) "
      + "ORDER BY id DESC "
      , nativeQuery = true)
  List<Feed> findNewestAll();
}
