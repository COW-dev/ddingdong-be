package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.FormListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FormListResponse(
    @Schema(description = "지원 폼지 ID", example = "1")
    Long formId,
    @Schema(description = "지원 폼지 제목", example = "폼지 제목입니다.")
    String title,
    @Schema(description = "지원 폼지 시작일", example = "2001-01-01")
    LocalDate startDate,
    @Schema(description = "지원 폼지 종료일", example = "2001-01-02")
    LocalDate endDate,
    @Schema(description = "활성화 여부", example = "true")
    boolean isActive
) {

  public static FormListResponse from(FormListQuery query) {
    return FormListResponse.builder()
        .formId(query.formId())
        .title(query.title())
        .startDate(query.startDate())
        .endDate(query.endDate())
        .isActive(query.isActive())
        .build();
  }
}
