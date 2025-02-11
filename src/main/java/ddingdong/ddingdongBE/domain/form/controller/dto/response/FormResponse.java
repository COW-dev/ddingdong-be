package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery.FormFieldListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record FormResponse(
        @Schema(description = "폼지 제목", example = "카우 1기 폼지")
        String title,
        @Schema(description = "폼지 설명", example = "폼지 설명입니다")
        String description,
        @Schema(description = "폼지 시작일", example = "2001-01-01")
        LocalDate startDate,
        @Schema(description = "폼지 종료일", example = "2001-01-02")
        LocalDate endDate,
        @Schema(description = "면접 여부", example = "true")
        boolean hasInterview,
        @Schema(description = "생성한 섹션들", example = "[공통,서버,웹]")
        List<String> sections,
        @ArraySchema(schema = @Schema(implementation = FormFieldListResponse.class))
        List<FormFieldListResponse> formFields
) {

    @Builder
    record FormFieldListResponse(
            @Schema(description = "폼지 질문", example = "당신의 이름은?")
            String question,
            @Schema(description = "폼지 질문 유형", example = "CHECK_BOX")
            FieldType type,
            @Schema(description = "폼지 지문", example = "[지문1, 지문2]")
            List<String> options,
            @Schema(description = "필수 여부", example = "true")
            boolean required,
            @Schema(description = "폼지 질문 순서", example = "1")
            int order,
            @Schema(description = "폼지 섹션", example = "공통")
            String section
    ) {

        public static FormFieldListResponse from(FormFieldListQuery formFieldListQuery) {
            return FormFieldListResponse.builder()
                    .question(formFieldListQuery.question())
                    .type(formFieldListQuery.type())
                    .options(formFieldListQuery.options())
                    .required(formFieldListQuery.required())
                    .order(formFieldListQuery.order())
                    .section(formFieldListQuery.section())
                    .build();
        }
    }

    public static FormResponse from(FormQuery formQuery) {
        List<FormFieldListResponse> responses = formQuery.formFields().stream()
                .map(FormFieldListResponse::from)
                .toList();

        return FormResponse.builder()
                .title(formQuery.title())
                .description(formQuery.description())
                .startDate(formQuery.startDate())
                .endDate(formQuery.endDate())
                .hasInterview(formQuery.hasInterview())
                .sections(formQuery.sections())
                .formFields(responses)
                .build();
    }
}
