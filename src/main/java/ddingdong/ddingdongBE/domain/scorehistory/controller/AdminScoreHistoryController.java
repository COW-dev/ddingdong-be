package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.scorehistory.api.AdminScoreHistoryApi;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.RegisterScoreRequest;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse.ScoreHistoryResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.ScoreHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminScoreHistoryController implements AdminScoreHistoryApi {

    private final ClubService clubService;
    private final ScoreHistoryService scoreHistoryService;

    public void register(Long clubId, RegisterScoreRequest registerScoreRequest) {
        scoreHistoryService.register(clubId, registerScoreRequest);
    }

    public ScoreHistoryFilterByClubResponse getScoreHistories(Long clubId) {
        Club club = clubService.getByClubId(clubId);
        List<ScoreHistoryResponse> scoreHistoryResponses = scoreHistoryService.getScoreHistories(clubId).stream()
                .map(ScoreHistoryResponse::from)
                .toList();
        return ScoreHistoryFilterByClubResponse.of(club, scoreHistoryResponses);
    }
}
