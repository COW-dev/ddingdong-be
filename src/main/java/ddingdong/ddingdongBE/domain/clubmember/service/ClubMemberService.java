package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import java.util.List;

public interface ClubMemberService {

    ClubMember getById(Long clubMemberId);

    void saveAll(List<ClubMember> clubMembers);

    void deleteAll(List<ClubMember> clubMembers);
}
