package ddingdong.ddingdongBE.domain.calendar.controller.dto.request;

import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(

        @Schema(description = "카테고리명", example = "활동보고서")
        @NotBlank(message = "카테고리명은 필수입니다.")
        String categoryName,

        @Schema(description = "카테고리 색상", example = "#FFFFFF")
        @NotBlank(message = "색상은 필수입니다.")
        String color

) {
        public CreateCategoryCommand toCommand() {
                return new CreateCategoryCommand(categoryName, color);
        }
}