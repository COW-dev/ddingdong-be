package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminClubFeedRankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed - Admin", description = "Feed Admin API")
@RequestMapping("/server/admin/feeds")
public interface AdminFeedApi {

    @Operation(summary = "총동연 피드 랭킹 조회 API")
    @ApiResponse(responseCode = "200", description = "동아리별 피드 랭킹 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminClubFeedRankingResponse.class))))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/ranking")
    List<AdminClubFeedRankingResponse> getClubFeedRanking(
            @RequestParam("year") @Min(value = 2000, message = "year는 2000 이상이어야 합니다.") @Max(value = 2100, message = "year는 2100 이하여야 합니다.") int year,
            @RequestParam("month") @Min(value = 1, message = "month는 1 이상이어야 합니다.") @Max(value = 12, message = "month는 12 이하여야 합니다.") int month
    );
}
