package ddingdong.ddingdongBE.domain.scorehistory.api;

import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request.RegisterScoreRequest;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ScoreHistoryFilterByClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ScroeHistory - Admin", description = "ScoreHistory Admin API")
@RequestMapping("/server/admin/{clubId}/score")
public interface AdminScoreHistoryApi {

    @Operation(summary = "어드민 점수 등록 API")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void register(@PathVariable Long clubId, @RequestBody RegisterScoreRequest registerScoreRequest);

    @Operation(summary = "어드민 동아리 점수 내역 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    ScoreHistoryFilterByClubResponse getScoreHistories(@PathVariable Long clubId);

}
