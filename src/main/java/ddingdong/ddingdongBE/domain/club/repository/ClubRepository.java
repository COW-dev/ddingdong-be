package ddingdong.ddingdongBE.domain.club.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

}
