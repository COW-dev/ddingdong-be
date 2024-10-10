package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.scorehistory.api.ClubScoreHistoryApi;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ClubScoreHistoryListResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.FacadeClubScoreHistoryService;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.ClubScoreHistoryListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubScoreHistoryController implements ClubScoreHistoryApi {

    private final FacadeClubScoreHistoryService facadeClubScoreHistoryService;

    public ClubScoreHistoryListResponse findMyScoreHistories(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        ClubScoreHistoryListQuery query = facadeClubScoreHistoryService.findMyScoreHistories(user.getId());
        return ClubScoreHistoryListResponse.from(query);
    }
}
