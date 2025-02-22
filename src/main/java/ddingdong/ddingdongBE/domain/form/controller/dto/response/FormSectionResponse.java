package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.FormSectionQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record FormSectionResponse(
        @Schema(description = "폼지 제목", example = "카우 1기 폼지")
        String title,
        @Schema(description = "폼지 설명", example = "폼지 설명입니다")
        String description,
        @Schema(description = "폼지 시작일", example = "2001-01-01")
        LocalDate startDate,
        @Schema(description = "폼지 종료일", example = "2001-01-02")
        LocalDate endDate,
        @Schema(description = "섹션 리스트", example = "[서버, 웹]")
        List<String> sections
) {

    public static FormSectionResponse from(FormSectionQuery formSectionQuery) {
        return FormSectionResponse.builder()
                .title(formSectionQuery.title())
                .description(formSectionQuery.description())
                .startDate(formSectionQuery.startDate())
                .endDate(formSectionQuery.endDate())
                .sections(formSectionQuery.sections())
                .build();
    }
}
