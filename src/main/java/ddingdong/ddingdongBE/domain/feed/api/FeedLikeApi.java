package ddingdong.ddingdongBE.domain.feed.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Feed Like", description = "피드 좋아요 API")
@RequestMapping("/server/feeds")
public interface FeedLikeApi {

    String UUID_V4_REGEXP = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Operation(summary = "피드 좋아요 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "피드 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 UUID 형식"),
            @ApiResponse(responseCode = "404", description = "피드 없음"),
            @ApiResponse(responseCode = "409", description = "이미 좋아요한 피드")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{feedId}/likes")
    void createLike(
            @PathVariable("feedId") Long feedId,
            @Pattern(regexp = UUID_V4_REGEXP, message = "유효하지 않은 UUID v4 형식입니다.")
            @RequestHeader("X-Anonymous-UUID") String uuid
    );

    @Operation(summary = "피드 좋아요 취소 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "피드 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 UUID 형식"),
            @ApiResponse(responseCode = "404", description = "좋아요 기록 없음")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{feedId}/likes")
    void deleteLike(
            @PathVariable("feedId") Long feedId,
            @Pattern(regexp = UUID_V4_REGEXP, message = "유효하지 않은 UUID v4 형식입니다.")
            @RequestHeader("X-Anonymous-UUID") String uuid
    );
}
