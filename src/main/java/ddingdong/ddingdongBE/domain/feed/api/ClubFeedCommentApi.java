package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed Comment - Club", description = "동아리 피드 댓글 관리 API")
@RequestMapping("/server/central/feeds")
public interface ClubFeedCommentApi {

    @Operation(summary = "피드 댓글 강제삭제 API (동아리 회장)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 강제삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글 없음")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{feedId}/comments/{commentId}")
    void forceDeleteComment(
            @PathVariable("feedId") Long feedId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
