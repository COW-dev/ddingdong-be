package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import java.util.List;

public interface FacadeAdminClubService {

    Long create(CreateClubCommand command);

    List<AdminClubListQuery> findAll();

    void deleteClub(Long clubId);
}
