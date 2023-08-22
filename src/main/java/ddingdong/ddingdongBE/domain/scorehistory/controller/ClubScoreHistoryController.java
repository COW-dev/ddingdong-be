package ddingdong.ddingdongBE.domain.scorehistory.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import ddingdong.ddingdongBE.domain.scorehistory.service.ScoreHistoryService;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/club/my/score")
public class ClubScoreHistoryController {

    private final ScoreHistoryService scoreHistoryService;

    @GetMapping
    public List<ScoreHistoryFilterByClubResponse> getMyScoreHistories(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return scoreHistoryService.getMyScoreHistories(principalDetails.getUser().getId());
    }
}