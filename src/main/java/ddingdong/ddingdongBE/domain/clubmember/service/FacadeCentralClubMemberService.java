package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.query.CentralClubMemberListQuery;
import java.util.List;

public interface FacadeCentralClubMemberService {

    byte[] getClubMemberListFile(Long userId);

    List<CentralClubMemberListQuery> getAllMyClubMember(Long userId);

    void updateMemberList(UpdateClubMemberListCommand command);

    void update(UpdateClubMemberCommand updateClubMemberCommand);

}
