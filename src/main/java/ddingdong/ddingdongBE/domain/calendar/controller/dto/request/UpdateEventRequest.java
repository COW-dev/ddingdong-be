package ddingdong.ddingdongBE.domain.calendar.controller.dto.request;

import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateEventRequest(

        @Schema(description = "이벤트 제목", example = "활동보고서 1차")
        @NotBlank(message = "이벤트명은 필수입니다.")
        String title,

        @Schema(description = "시작일자", example = "2026-10-02")
        @NotNull(message = "시작일은 필수입니다.")
        LocalDate startDate,

        @Schema(description = "종료일자", example = "2026-11-01")
        @NotNull(message = "종료일은 필수입니다.")
        LocalDate endDate,

        @Schema(description = "반복 종료일자", example = "2026-12-31")
        @NotNull(message = "반복 종료일은 필수입니다.")
        LocalDate repeatEndDate,

        @Schema(description = "반복 형식", example = "NONE")
        @NotNull(message = "반복 유형은 필수입니다.")
        RepeatType repeatType,

        @Schema(description = "카테고리명", example = "활동보고서")
        @NotBlank(message = "카테고리는 필수입니다.")
        String category
) {
        public UpdateEventCommand toCommand() {
                return new UpdateEventCommand(title, startDate, endDate, repeatEndDate, repeatType, category);
        }
}
