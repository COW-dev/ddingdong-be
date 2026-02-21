package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminFeedRankingWinnerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed - Admin", description = "Feed Admin API")
@RequestMapping("/server/admin/feeds")
public interface AdminFeedApi {

    @Operation(summary = "역대 1위 동아리 조회 API")
    @ApiResponse(responseCode = "200", description = "역대 1위 동아리 조회 성공",
            content = @Content(schema = @Schema(implementation = AdminFeedRankingWinnerResponse.class)))
    @ApiResponse(responseCode = "404", description = "해당 연도 피드 없음")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/ranking/last")
    AdminFeedRankingWinnerResponse getYearlyWinner(
            @RequestParam("year") @Min(value = 2000, message = "year는 2000 이상이어야 합니다.") @Max(value = 2100, message = "year는 2100 이하여야 합니다.") int year
    );
}
