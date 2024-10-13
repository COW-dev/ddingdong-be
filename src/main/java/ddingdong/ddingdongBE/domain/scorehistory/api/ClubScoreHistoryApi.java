package ddingdong.ddingdongBE.domain.scorehistory.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.common.exception.ErrorResponse;
import ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response.ClubScoreHistoryListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ScoreHistory - Club", description = "ScoreHistory Club API")
@RequestMapping("/server/club/my/score")
public interface ClubScoreHistoryApi {

    @Operation(summary = "동아리 내 점수 내역 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "점수 변동 내역 목록 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClubScoreHistoryListResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "존재하지 않는 동아리",
                                            value = """
                                                    {
                                                      "status": 400,
                                                      "message": "존재하지 않는 동아리입니다.",
                                                      "timestamp": "2024-08-22T00:08:46.990585"
                                                    }
                                                  """
                                    )
                            })
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    ClubScoreHistoryListResponse findMyScoreHistories(@AuthenticationPrincipal PrincipalDetails principalDetails);

}
