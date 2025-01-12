package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.domain.feed.controller.dto.response.ClubFeedPageResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedPerClubPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed - User", description = "Feed API")
@RequestMapping("/server")
public interface FeedApi {

    @Operation(summary = "특정 동아리 피드 페이지 조회 API")
    @ApiResponse(responseCode = "200", description = "특정 동아리 피드 페이지 조회 성공",
        content = @Content(schema = @Schema(implementation = ClubFeedPageResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clubs/{clubId}/feeds")
    ClubFeedPageResponse getFeedPageByClub(
        @PathVariable("clubId") Long clubId,
        @RequestParam(value = "size", defaultValue = "9") int size,
        @RequestParam(value = "currentCursorId", defaultValue = "-1") Long currentCursorId
        );

    @Operation(summary = "모든 동아리 최신 피드 페이지 조회 API")
    @ApiResponse(responseCode = "200", description = "모든 동아리 최신 피드 페이지 조회 성공",
        content = @Content(schema = @Schema(implementation = NewestFeedPerClubPageResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/feeds")
    NewestFeedPerClubPageResponse getNewestFeedPerClub(
        @RequestParam(value = "size", defaultValue = "9") int size,
        @RequestParam(value = "currentCursorId", defaultValue = "-1") Long currentCursorId
    );

    @Operation(summary = "동아리 피드 상세 조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 피드 상세 조회 API",
        content = @Content(schema = @Schema(implementation = FeedResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/feeds/{feedId}")
    FeedResponse getByFeedId(@PathVariable("feedId") Long feedId);
}
