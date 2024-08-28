package ddingdong.ddingdongBE.domain.clubpost.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.ClubFeedResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.CreateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.request.UpdateClubPostRequest;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostListResponse;
import ddingdong.ddingdongBE.domain.clubpost.controller.dto.response.ClubPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Club - Post", description = "Club Post API")
@RequestMapping("/server/club/posts")
public interface ClubPostAPI {

  @Operation(summary = "동아리 게시물 생성 API")
  @ApiResponse(responseCode = "201", description = "동아리 게시물 생성 성공")
  @ResponseStatus(HttpStatus.CREATED)
  @SecurityRequirement(name = "AccessToken")
  @PostMapping
  void createClubPost(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestBody @Valid CreateClubPostRequest request
  );

  @Operation(summary = "동아리 게시물 수정 API")
  @ApiResponse(responseCode = "200", description = "동아리 게시물 수정 성공")
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "AccessToken")
  @PutMapping("/{clubPostId}")
  void updateClubPost(
      @PathVariable Long clubPostId,
      @RequestBody @Valid UpdateClubPostRequest request);

  @Operation(summary = "동아리 게시물 삭제 API")
  @ApiResponse(responseCode = "200", description = "동아리 게시물 삭제 성공")
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "AccessToken")
  @DeleteMapping("/{clubPostId}")
  void deleteClubPost(@PathVariable Long clubPostId);

  @Operation(summary = "동아리 게시물 상세 조회 API")
  @ApiResponse(responseCode = "200", description = "동아리 게시물 상세 조회 성공",
  content = @Content(schema = @Schema(implementation = ClubPostResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{clubPostId}")
  ClubPostResponse getClubPost(@PathVariable Long clubPostId);

  @Operation(summary = "동아리 게시물 전체 조회 API")
  @ApiResponse(responseCode = "200", description = "동아리 게시물 전체 조회 성공",
      content = @Content(schema = @Schema(implementation = ClubPostListResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/{clubId}")
  ClubPostListResponse getClubPosts(@PathVariable Long clubId);

  @Operation(summary = "동아리 전체 피드 조회 API")
  @ApiResponse(responseCode = "200", description = "동아리 전체 피드 조회 성공",
      content = @Content(schema = @Schema(implementation = ClubFeedResponse.class)))
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  ClubFeedResponse getClubFeeds();
}
