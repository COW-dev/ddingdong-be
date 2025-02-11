package ddingdong.ddingdongBE.domain.formapplication.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.query.FormApplicationQuery.FormFieldAnswerListQuery;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FormApplicationResponse(
        @Schema(description = "폼지 면접 여부", example = "true")
        boolean hasInterview,
        @Schema(description = "제출일시", example = "2025-01-01T00:00")
        LocalDateTime submittedAt,
        @Schema(description = "지원자 이름", example = "김띵동")
        String name,
        @Schema(description = "지원자 학번", example = "60201111")
        String studentNumber,
        @Schema(description = "지원자 학과", example = "융합소프트웨어학부")
        String department,
        @Schema(description = "status", example = "SUBMITTED")
        FormApplicationStatus status,
        @ArraySchema(schema = @Schema(implementation = FormFieldAnswerListResponse.class))
        List<FormFieldAnswerListResponse> formFieldAnswers
) {

    @Builder
    record FormFieldAnswerListResponse(
            @Schema(description = "폼지 질문 ID", example = "1")
            Long fieldId,
            @Schema(description = "폼지 질문", example = "성별이 무엇입니까??")
            String question,
            @Schema(description = "폼지 질문 유형", example = "RADIO", allowableValues = {"CHECK_BOX",
                    "RADIO",
                    "TEXT", "LONG_TEXT", "FILE"})
            FieldType type,
            @Schema(description = "폼지 지문", example = "[\"여성\", \"남성\"]")
            List<String> options,
            @Schema(description = "필수 여부", example = "true")
            Boolean required,
            @Schema(description = "질문 순서", example = "1")
            Integer order,
            @Schema(description = "섹션", example = "공통")
            String section,
            @Schema(description = "질문 답변 값", example = "[\"지문1\"]")
            List<String> value
    ) {

        public static FormFieldAnswerListResponse from(
                FormFieldAnswerListQuery formFieldAnswerListQuery) {
            return FormFieldAnswerListResponse.builder()
                    .fieldId(formFieldAnswerListQuery.fieldId())
                    .question(formFieldAnswerListQuery.question())
                    .type(formFieldAnswerListQuery.type())
                    .options(formFieldAnswerListQuery.options())
                    .required(formFieldAnswerListQuery.required())
                    .order(formFieldAnswerListQuery.order())
                    .section(formFieldAnswerListQuery.section())
                    .value(formFieldAnswerListQuery.value())
                    .build();
        }
    }

    public static FormApplicationResponse from(FormApplicationQuery formApplicationQuery) {
        List<FormFieldAnswerListResponse> responses = formApplicationQuery.formFieldAnswers()
                .stream()
                .map(FormFieldAnswerListResponse::from)
                .toList();

        return FormApplicationResponse.builder()
                .hasInterview(formApplicationQuery.hasInterview())
                .submittedAt(formApplicationQuery.createdAt())
                .name(formApplicationQuery.name())
                .studentNumber(formApplicationQuery.studentNumber())
                .department(formApplicationQuery.department())
                .status(formApplicationQuery.status())
                .formFieldAnswers(responses)
                .build();
    }
}
