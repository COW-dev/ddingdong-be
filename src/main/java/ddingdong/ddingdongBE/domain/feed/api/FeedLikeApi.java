package ddingdong.ddingdongBE.domain.feed.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed Like", description = "피드 좋아요 API")
@RequestMapping("/server/feeds")
public interface FeedLikeApi {

    @Operation(summary = "피드 좋아요 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "피드 좋아요 성공"),
            @ApiResponse(responseCode = "404", description = "피드 없음")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{feedId}/likes")
    void createLike(@PathVariable("feedId") Long feedId);
}
