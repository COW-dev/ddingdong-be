package ddingdong.ddingdongBE.domain.notice.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ddingdong.ddingdongBE.domain.notice.controller.dto.request.GetAllNoticeByPageRequest;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.DetailNoticeResponse;
import ddingdong.ddingdongBE.domain.notice.controller.dto.response.GetAllNoticeByPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Notice", description = "Notice API")
@RequestMapping(value = "/server/notices", produces = APPLICATION_JSON_VALUE)
public interface NoticeApi {

    @Operation(summary = "공지사항 전체 조회 API")
    @GetMapping
    GetAllNoticeByPageResponse getAllNotices(
        GetAllNoticeByPageRequest request
    );

    @Operation(summary = "공지사항 상세 조회 API")
    @GetMapping("/{noticeId}")
    DetailNoticeResponse getNoticeDetail(@PathVariable Long noticeId);
}
