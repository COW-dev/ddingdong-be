package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.domain.scorehistory.api.AdminScoreHistoryApi;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.CreateScoreHistoryRequest;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryListResponse;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryListResponse.ScoreHistoryResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.FacadeAdminScoreHistoryService;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.AdminClubScoreHistoryListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminScoreHistoryController implements AdminScoreHistoryApi {

    private final FacadeAdminScoreHistoryService facadeAdminScoreHistoryService;

    public void createScoreHistory(Long clubId, CreateScoreHistoryRequest createScoreHistoryRequest) {
        facadeAdminScoreHistoryService.create(createScoreHistoryRequest.toCommand(clubId));
    }

    public ScoreHistoryListResponse findClubScoreHistories(Long clubId) {
        AdminClubScoreHistoryListQuery query = facadeAdminScoreHistoryService.findAllByClubId(clubId);
        return ScoreHistoryListResponse.of(query);
    }
}
