package ddingdong.ddingdongBE.domain.clubpost.repository;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

  @Query(value = "SELECT * FROM club_post WHERE club_id = :clubId AND deleted_at IS NULL ORDER BY id"
      , nativeQuery = true)
  List<ClubPost> findAllByClubIdOrderById(@Param("clubId") Long clubId);

  @Query(value = "SELECT * FROM club_post p WHERE p.id in "
      + "(SELECT min(id) "
      + "FROM club_post cp "
      + "WHERE deleted_at IS NULL "
      + "GROUP BY club_id) "
      + "ORDER BY id"
      , nativeQuery = true)
  List<ClubPost> findLatestGroupByClub();

}
