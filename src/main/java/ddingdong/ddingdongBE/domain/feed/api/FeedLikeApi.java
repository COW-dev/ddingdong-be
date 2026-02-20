package ddingdong.ddingdongBE.domain.feed.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed Like", description = "피드 좋아요 API")
@RequestMapping("/server/feeds")
public interface FeedLikeApi {

    @Operation(summary = "피드 좋아요 API")
    @ApiResponse(responseCode = "201", description = "피드 좋아요 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/{feedId}/likes")
    void createLike(
            @PathVariable("feedId") Long feedId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}
