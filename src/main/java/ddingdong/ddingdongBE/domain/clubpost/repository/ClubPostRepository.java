package ddingdong.ddingdongBE.domain.clubpost.repository;

import ddingdong.ddingdongBE.domain.clubpost.entity.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
}
