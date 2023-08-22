package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.RegisterScoreRequest;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.ScoreHistoryService;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/{clubId}/score")
public class AdminScoreHistoryController {

    private final ScoreHistoryService scoreHistoryService;

    @PostMapping
    public void register(
            @PathVariable Long clubId,
            @RequestBody RegisterScoreRequest registerScoreRequest
    ) {
        scoreHistoryService.register(clubId, registerScoreRequest);
    }

    @GetMapping
    public List<ScoreHistoryFilterByClubResponse> getScoreHistories(
            @PathVariable Long clubId
    ) {
        return scoreHistoryService.getScoreHistories(clubId);
    }
}
