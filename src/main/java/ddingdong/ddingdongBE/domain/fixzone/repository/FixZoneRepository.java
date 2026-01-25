package ddingdong.ddingdongBE.domain.fixzone.repository;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FixZoneRepository extends JpaRepository<FixZone, Long> {

    List<FixZone> findAllByClubId(Long clubId);

    @Query(value = """
            SELECT fz.* FROM fix_zone fz
            INNER JOIN club c ON fz.club_id = c.id
            WHERE fz.deleted_at IS NULL
            AND c.deleted_at IS NULL
            """, nativeQuery = true)
    List<FixZone> findAllWithActiveClub();

    void deleteAllByClubId(Long clubId);

}
