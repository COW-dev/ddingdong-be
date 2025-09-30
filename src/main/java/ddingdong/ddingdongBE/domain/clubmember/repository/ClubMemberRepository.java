package ddingdong.ddingdongBE.domain.clubmember.repository;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findByIdIn(Collection<Long> ids);

    List<ClubMember> findByClubId(Long clubId);
}
