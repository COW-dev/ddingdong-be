package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.AllClubMemberInfoQuery;

public interface FacadeCentralClubMemberService {

    byte[] getClubMemberListFile(Long userId);

    AllClubMemberInfoQuery getAllMyClubMember(Long userId);

    void updateMemberList(UpdateClubMemberListCommand command);

    void update(UpdateClubMemberCommand updateClubMemberCommand);

    void delete(Long userId, Long clubMemberId);
}
