package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import java.time.LocalDate;
import java.util.List;

public interface FacadeUserClubService {

    List<UserClubListQuery> findAllWithRecruitTimeCheckPoint(LocalDate now);

    UserClubQuery getClub(Long clubId);

}
