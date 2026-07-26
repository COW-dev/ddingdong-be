package ddingdong.ddingdongBE.domain.calendar.controller.dto.response;

import ddingdong.ddingdongBE.domain.calendar.service.dto.query.CategoryQuery;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(description = "카테고리 아이디", example = "1")
        Long id,

        @Schema(description = "카테고리명", example = "활동보고서")
        String name,

        @Schema(description = "카테고리 색상", example = "#FFFFFF")
        String color
) {

    public static CategoryResponse from(CategoryQuery query) {
        return new CategoryResponse(query.id(), query.name(), query.color());
    }
}
