package ddingdong.ddingdongBE.domain.notice.api;

import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.NoticeListResponse;
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

@Tag(name = "Notice - User", description = "Notice API")
@RequestMapping("/server/notices")
public interface NoticeApi {

    @Operation(summary = "공지사항 전체 조회")
    @ApiResponse(responseCode = "200", description = "공지사항 전체 조회 성공",
        content = @Content(schema = @Schema(implementation = NoticeListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<NoticeListResponse> getNotices();

    @Operation(summary = "공지사항 상세 조회")
    @ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공",
        content = @Content(schema = @Schema(implementation = NoticeResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{noticeId}")
    NoticeResponse getNotice(@PathVariable Long noticeId);
}
