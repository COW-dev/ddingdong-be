package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;

public interface FacadeCentralClubService {

    MyClubInfoQuery getMyClubInfo(Long userId);

    Long updateClubInfo(UpdateClubInfoCommand command);

}
