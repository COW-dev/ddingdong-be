package ddingdong.ddingdongBE.domain.activityreport.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(
        name = "CreateActivityTermInfoRequest",
        description = "활동 보고서 회차 시작 기준일 설정 요청"
)
public record CreateActivityTermInfoRequest(
        @Schema(description = "활동 보고서 시작 일자", example = "2024-07-22")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
        LocalDate startDate,
        @Schema(description = "설정할 총 회차 수", example = "10 (=총 10회 설정)")
        int totalTermCount
) {
}
