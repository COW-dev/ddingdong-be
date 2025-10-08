package ddingdong.ddingdongBE.domain.clubmember.repository;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findByIdIn(Collection<Long> ids);

    List<ClubMember> findByClubId(Long clubId);

    @Modifying
    @Query("UPDATE ClubMember cm SET cm.deletedAt = CURRENT_TIMESTAMP WHERE cm.club.id = :clubId AND cm.deletedAt IS NULL")
    void deleteAllByClubId(Long clubId);
}
