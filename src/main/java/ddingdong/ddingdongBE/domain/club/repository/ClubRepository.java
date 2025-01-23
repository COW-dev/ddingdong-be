package ddingdong.ddingdongBE.domain.club.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"clubMembers"})
    Optional<Club> findEntityGraphByUserId(Long userId);
}
