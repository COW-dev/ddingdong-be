package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.feed.controller.dto.request.CreateFeedRequest;
import ddingdong.ddingdongBE.domain.feed.controller.dto.request.UpdateFeedRequest;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.MyFeedPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed - Club", description = "Feed API")
@RequestMapping("/server/central")
public interface ClubFeedApi {

    @Operation(summary = "동아리 피드 생성 API")
    @ApiResponse(responseCode = "201", description = "동아리 피드 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/my/feeds")
    void createFeed(
        @RequestBody @Valid CreateFeedRequest createFeedRequest,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 피드 수정 API")
    @ApiResponse(responseCode = "204", description = "동아리 피드 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PutMapping("/my/feeds/{feedId}")
    void updateFeed(
        @PathVariable("feedId") Long feedId,
        @RequestBody @Valid UpdateFeedRequest updateFeedRequest
    );

    @Operation(summary = "동아리 피드 삭제 API")
    @ApiResponse(responseCode = "204", description = "동아리 피드 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/my/feeds/{feedId}")
    void deleteFeed(
        @PathVariable("feedId") Long feedId
    );

    @Operation(summary = "중앙 동아리 피드 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "중앙 동아리 피드 목록 조회 성공",
        content = @Content(schema = @Schema(implementation = MyFeedPageResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/feeds")
    MyFeedPageResponse getMyFeedPage(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(value = "size", defaultValue = "9") int size,
        @RequestParam(value = "currentCursorId", defaultValue = "-1") Long currentCursorId
    );
}
