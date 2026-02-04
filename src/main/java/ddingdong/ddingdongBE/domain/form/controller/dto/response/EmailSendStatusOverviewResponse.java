package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusOverviewQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusOverviewQuery.EmailSendStatusOverviewInfoQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record EmailSendStatusOverviewResponse(
        List<EmailSendStatusOverviewInfoResponse> emailSendStatusOverviewInfoResponses
) {

    public static EmailSendStatusOverviewResponse from(EmailSendStatusOverviewQuery query) {
        List<EmailSendStatusOverviewInfoResponse> responses =
                query.emailSendStatusOverviewInfoQueries()
                        .stream()
                        .map(EmailSendStatusOverviewInfoResponse::from)
                        .toList();

        return new EmailSendStatusOverviewResponse(responses);
    }

    public record EmailSendStatusOverviewInfoResponse(
            @Schema(description = "지원 결과 상태", example = "FIRST_PASS")
            FormApplicationStatus formApplicationStatus,
            @Schema(description = "이메일 최근 전송 시각", example = "2026-01-23T12:55:33", nullable = true)
            LocalDateTime lastSentAt,
            @Schema(description = "이메일 전송 성공 건수", example = "2")
            int successCount,
            @Schema(description = "이메일 전송 실패 건수", example = "4")
            int failCount
    ) {

        public static EmailSendStatusOverviewInfoResponse from(
                EmailSendStatusOverviewInfoQuery query) {
            return new EmailSendStatusOverviewInfoResponse(
                    query.formApplicationStatus(),
                    query.lastSentAt(),
                    query.successCount(),
                    query.failCount()
            );
        }
    }
}