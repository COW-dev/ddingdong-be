package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed - User", description = "Feed API")
@RequestMapping("/server")
public interface FeedApi {

  @Operation(summary = "동아리 피드 전체 조회 API")
  @ApiResponse(responseCode = "200", description = "동아리 피드 전체 조회 성공",
      content = @Content(schema = @Schema(implementation = FeedListResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/clubs/{clubId}/feeds")
  List<FeedListResponse> getAllFeedByClubId(@PathVariable Long clubId);

  @Operation(summary = "전체 동아리 최신 피드 조회 API")
  @ApiResponse(responseCode = "200", description = "전체 동아리 최신 피드 조회 성공",
      content = @Content(schema = @Schema(implementation = NewestFeedListResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/feeds")
  List<NewestFeedListResponse> getNewestAllFeed();

  @Operation(summary = "동아리 피드 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "동아리 피드 상세 조회 API",
      content = @Content(schema = @Schema(implementation = FeedResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/feeds/{feedId}")
  FeedResponse getByFeedId(@PathVariable Long feedId);
}
