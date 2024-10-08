package ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.command.CreateScoreHistoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@Schema(
        name = "CreateScoreHistoryRequest",
        description = "어드민 - 동아리 점수 변동 내역 생성 "
)
@Builder
public record CreateScoreHistoryRequest(
        @Schema(description = "점수변동내역 카테고리",
                example = "ACTIVITY_REPORT",
                allowableValues = {"CLEANING", "ACTIVITY_REPORT", "LEADER_CONFERENCE", "BUSINESS_PARTICIPATION",
                        "ADDITIONAL", "CARRYOVER_SCORE"}
        )
        @NotNull(message = "점수변동내역 카테고리는 필수입니다.")
        String scoreCategory,

        @Schema(description = "점수변동내역 원인", example = "1회차 활동보고서 작성")
        String reason,

        @Schema(description = "변동 점수", example = "10")
        @NotNull(message = "변동 점수는 필수입니다.")
        BigDecimal amount
) {

    public CreateScoreHistoryCommand toCommand(Long clubId) {
        return CreateScoreHistoryCommand.builder()
                .clubId(clubId)
                .amount(amount)
                .scoreCategory(ScoreCategory.from(scoreCategory))
                .reason(reason)
                .build();
    }
}
