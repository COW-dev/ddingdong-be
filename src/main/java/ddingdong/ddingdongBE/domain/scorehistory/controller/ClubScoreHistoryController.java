package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.api.ClubScoreHistoryApi;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse.ScoreHistoryResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.ScoreHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubScoreHistoryController implements ClubScoreHistoryApi {

    private final ClubService clubService;
    private final ScoreHistoryService scoreHistoryService;

    public ScoreHistoryFilterByClubResponse getMyScoreHistories(PrincipalDetails principalDetails) {
        Club club = clubService.getByUserId(principalDetails.getUser().getId());
        List<ScoreHistoryResponse> scoreHistoryResponses = scoreHistoryService.getMyScoreHistories(club.getId())
                .stream()
                .map(ScoreHistoryResponse::from)
                .toList();
        return ScoreHistoryFilterByClubResponse.of(club, scoreHistoryResponses);
    }
}
