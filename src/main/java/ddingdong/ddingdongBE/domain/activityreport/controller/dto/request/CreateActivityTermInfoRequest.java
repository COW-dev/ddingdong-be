package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.command.CreateActivityTermInfoCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(
    name = "CreateActivityTermInfoRequest",
    description = "활동 보고서 회차 시작 기준일 설정 요청"
)
public record CreateActivityTermInfoRequest(
    @Schema(description = "활동 보고서 시작 일자", example = "2024-07-22")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "startDate는 null이 될 수 없습니다.")
    LocalDate startDate,

    @Schema(description = "설정할 총 회차 수", example = "10 (=총 10회 설정)")
    @Min(value = 1, message = "totalTermCount는 1 이상이어야 합니다.")
    int totalTermCount
) {

    public CreateActivityTermInfoCommand toCommand() {
        return CreateActivityTermInfoCommand.builder()
            .startDate(startDate)
            .totalTermCount(totalTermCount)
            .build();
    }
}
