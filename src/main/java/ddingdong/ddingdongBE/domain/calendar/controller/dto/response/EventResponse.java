package ddingdong.ddingdongBE.domain.calendar.controller.dto.response;

import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record EventResponse(

        @Schema(description = "이벤트 아이디", example = "1")
        Long id,

        @Schema(description = "이벤트 제목", example = "활동보고서 1차")
        String title,

        @Schema(description = "시작일자", example = "2026-10-02")
        LocalDate startDate,

        @Schema(description = "종료일자", example = "2026-11-01")
        LocalDate endDate,

        @Schema(description = "반복 형식", example = "NONE")
        RepeatType repeatType,

        @Schema(description = "카테고리명", example = "활동보고서")
        String category,

        @Schema(description = "카테고리 색상", example = "#FF0000")
        String color
) {
        public static EventResponse from(EventQuery query) {
                return new EventResponse(
                        query.id(),
                        query.title(),
                        query.startDate(),
                        query.endDate(),
                        query.repeatType(),
                        query.categoryName(),
                        query.color()
                );
        }

}
