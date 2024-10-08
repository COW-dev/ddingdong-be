package ddingdong.ddingdongBE.domain.clubmember.service;

import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberCommand;
import ddingdong.ddingdongBE.domain.clubmember.service.dto.command.UpdateClubMemberListCommand;

public interface FacadeClubMemberService {

    byte[] getClubMemberListFile(Long userId);

    void updateMemberList(UpdateClubMemberListCommand command);

    void update(UpdateClubMemberCommand updateClubMemberCommand);

}
