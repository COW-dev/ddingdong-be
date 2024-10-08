package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.scorehistory.api.ClubScoreHistoryApi;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryListResponse;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryListResponse.ScoreHistoryResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.FacadeClubScoreHistoryService;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.ClubScoreHistoryListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubScoreHistoryController implements ClubScoreHistoryApi {

    private final FacadeClubScoreHistoryService facadeClubScoreHistoryService;

    public ScoreHistoryListResponse findMyScoreHistories(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        ClubScoreHistoryListQuery query = facadeClubScoreHistoryService.findMyScoreHistories(user.getId());
        List<ScoreHistoryResponse> scoreHistoryResponses = query.scoreHistories().stream()
                .map(ScoreHistoryResponse::from)
                .toList();
        return ScoreHistoryListResponse.of(query.club(), scoreHistoryResponses);
    }
}
