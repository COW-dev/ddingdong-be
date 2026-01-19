package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendCountQuery;
import io.swagger.v3.oas.annotations.media.Schema;


public record EmailSendCountResponse(
        @Schema(description = "전체 이메일 개수", example = "40")
        int totalCount,
        @Schema(description = "전송 성공한 이메일 개수", example = "30")
        int successCount,
        @Schema(description = "전송 실패한 이메일 개수", example = "10")
        int failCount
) {

    public static EmailSendCountResponse from(EmailSendCountQuery query) {
        return new EmailSendCountResponse(
                query.totalCount(),
                query.successCount(),
                query.failCount()
        );
    }
}
