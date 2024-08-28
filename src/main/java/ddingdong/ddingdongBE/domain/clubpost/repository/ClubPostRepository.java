package ddingdong.ddingdongBE.domain.clubpost.repository;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

  @Query(value = "SELECT * FROM club_post WHERE club_id = :clubId ORDER BY created_at DESC", nativeQuery = true)
  List<ClubPost> findAllByClubIdOrderByCreatedAt(@Param("clubId") Long clubId);

  @Query(value = "SELECT * FROM club_post p WHERE p.created_at = ("
      + "SELECT MAX(p2.created_at)"
      + " FROM club_post p2"
      + " WHERE p2.club_id = p.club_id)"
      + "ORDER BY p.created_at")
  List<ClubPost> findLatestGroupByClub();
}
