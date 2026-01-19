package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusQuery.EmailSendStatusInfoQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record EmailSendStatusResponse(
        List<EmailSendStatusInfoResponse> emailSendStatusInfoResponses

) {

    public static EmailSendStatusResponse from(EmailSendStatusQuery emailSendStatusQuery) {
        List<EmailSendStatusInfoResponse> responses = emailSendStatusQuery.emailSendStatusInfoQueries()
                .stream()
                .map(EmailSendStatusInfoResponse::from)
                .toList();
        return new EmailSendStatusResponse(responses);
    }

    record EmailSendStatusInfoResponse(
            @Schema(description = "지원자 이름", example = "고건")
            String name,
            @Schema(description = "학번", example = "60221300")
            String studentNumber,
            @Schema(description = "이메일 전송 일시", example = "2025.12.13 13:00")
            LocalDateTime sendAt, // TODO: 재전송 시 Update 해주기
            @Schema(description = "이메일 전송 상태", example = "Sending")
            EmailSendStatus emailSendStatus,
            @Schema(description = "지원자 서류/면접 합불 상태", example = "FIRST_PASS")
            FormApplicationStatus formApplicationStatus
    ) {

        public static EmailSendStatusInfoResponse from(
                EmailSendStatusInfoQuery emailSendStatusInfoQuery) {
            return new EmailSendStatusInfoResponse(
                    emailSendStatusInfoQuery.name(),
                    emailSendStatusInfoQuery.studentNumber(),
                    emailSendStatusInfoQuery.sendAt(),
                    emailSendStatusInfoQuery.emailSendStatus(),
                    emailSendStatusInfoQuery.formApplicationStatus()
            );
        }
    }
}
