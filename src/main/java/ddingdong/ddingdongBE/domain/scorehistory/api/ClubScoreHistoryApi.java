package ddingdong.ddingdongBE.domain.scorehistory.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ScoreHistory - Club", description = "ScoreHistory Club API")
@RequestMapping("/server/club/my/score")
public interface ClubScoreHistoryApi {

    @Operation(summary = "동아리 내 점수 내역 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    ScoreHistoryFilterByClubResponse findMyScoreHistories(@AuthenticationPrincipal PrincipalDetails principalDetails);

}
