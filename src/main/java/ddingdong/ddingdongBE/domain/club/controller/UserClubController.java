package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.api.UserClubApi;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.UserClubListResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.UserClubResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeUserClubService;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserClubController implements UserClubApi {

    private final FacadeUserClubService facadeUserClubService;


    @Override
    public List<UserClubListResponse> getClubs() {
        return facadeUserClubService.findAllWithRecruitTimeCheckPoint(LocalDateTime.now()).stream()
                .map(UserClubListResponse::from)
                .toList();
    }

    @Override
    public UserClubResponse getDetailClub(Long clubId) {
        UserClubQuery query = facadeUserClubService.getClub(clubId);
        return UserClubResponse.from(query);
    }

}
