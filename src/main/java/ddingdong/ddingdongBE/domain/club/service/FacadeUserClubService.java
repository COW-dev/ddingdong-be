package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import java.time.LocalDateTime;
import java.util.List;

public interface FacadeUserClubService {

    List<UserClubListQuery> findAllWithRecruitTimeCheckPoint(LocalDateTime now);

    UserClubQuery getClub(Long clubId);

}
