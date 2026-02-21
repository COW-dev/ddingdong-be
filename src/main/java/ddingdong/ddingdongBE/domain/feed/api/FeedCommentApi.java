package ddingdong.ddingdongBE.domain.feed.api;

import static ddingdong.ddingdongBE.common.constant.ValidationConstants.UUID_V4_REGEXP;

import ddingdong.ddingdongBE.domain.feed.controller.dto.request.CreateFeedCommentRequest;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.CreateFeedCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed Comment", description = "피드 댓글 API (비회원)")
@RequestMapping("/server/feeds")
public interface FeedCommentApi {

    @Operation(summary = "피드 댓글 작성 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공",
                    content = @Content(schema = @Schema(implementation = CreateFeedCommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "피드 없음")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{feedId}/comments")
    CreateFeedCommentResponse createComment(
            @PathVariable("feedId") Long feedId,
            @Pattern(regexp = UUID_V4_REGEXP, message = "유효하지 않은 UUID v4 형식입니다.")
            @RequestHeader("X-Anonymous-UUID") String uuid,
            @RequestBody @Valid CreateFeedCommentRequest request
    );

    @Operation(summary = "피드 댓글 삭제 API (비회원)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 UUID 형식"),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 없음")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{feedId}/comments/{commentId}")
    void deleteComment(
            @PathVariable("feedId") Long feedId,
            @PathVariable("commentId") Long commentId,
            @Pattern(regexp = UUID_V4_REGEXP, message = "유효하지 않은 UUID v4 형식입니다.")
            @RequestHeader("X-Anonymous-UUID") String uuid
    );
}
