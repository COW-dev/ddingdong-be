package ddingdong.ddingdongBE.domain.calendar.controller.dto.request;

import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateCategoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(

        @Schema(description = "카테고리명", example = "동아리 일정")
        @NotBlank(message = "카테고리명은 필수입니다.")
        String categoryName,

        @Schema(description = "카테고리 색상", example = "#FFFFFF")
        @NotBlank(message = "색상은 필수입니다.")
        String color

) {

    public UpdateCategoryCommand toCommand() {
        return new UpdateCategoryCommand(categoryName, color);
    }
}
