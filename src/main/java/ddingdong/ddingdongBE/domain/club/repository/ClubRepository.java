package ddingdong.ddingdongBE.domain.club.repository;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.dto.UserClubListInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"clubMembers"})
    Optional<Club> findEntityGraphByUserId(Long userId);

    @Query(value = """
            SELECT c.id, c.name, c.category, c.tag, f.start_date AS start, f.end_date AS end
                FROM club c
                LEFT JOIN (
                    SELECT f1.id, f1.club_id, f1.start_date, f1.end_date
                    FROM form f1
                    WHERE f1.id = (
                            SELECT f2.id
                            FROM form f2
                            WHERE f2.club_id = f1.club_id
                            ORDER BY LEAST(
                                COALESCE(ABS(DATEDIFF(f2.start_date, :now)), 99999),
                                COALESCE(ABS(DATEDIFF(f2.end_date, :now)), 99999)
                            )
                            LIMIT 1
                    )
                ) f ON c.id = f.club_id
                WHERE deleted_at IS NULL;
            """, nativeQuery = true)
    List<UserClubListInfo> findAllClubListInfo(@Param("now") LocalDate now);
}
