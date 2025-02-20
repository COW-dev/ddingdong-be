package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery.UserFormFieldListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record UserFormResponse(
        @Schema(description = "동아리 이름", example = "카우")
        String clubName,
        @Schema(description = "폼지 제목", example = "카우 1기 폼지")
        String title,
        @Schema(description = "폼지 설명", example = "폼지 설명입니다")
        String description,
        @Schema(description = "폼지 지원자 수", example = "20")
        int applicationCount,
        @Schema(description = "폼지 시작일", example = "2001-01-01")
        LocalDate startDate,
        @Schema(description = "폼지 종료일", example = "2001-01-02")
        LocalDate endDate,
        @ArraySchema(schema = @Schema(implementation = UserFormFieldListResponse.class))
        List<UserFormFieldListResponse> formFields
) {

    @Builder
    record UserFormFieldListResponse(
            @Schema(description = "폼지 질문 id", example = "6")
            Long id,
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

        public static UserFormFieldListResponse from(UserFormFieldListQuery formFieldListQuery) {
            return UserFormFieldListResponse.builder()
                    .id(formFieldListQuery.id())
                    .question(formFieldListQuery.question())
                    .type(formFieldListQuery.type())
                    .options(formFieldListQuery.options())
                    .required(formFieldListQuery.required())
                    .order(formFieldListQuery.order())
                    .section(formFieldListQuery.section())
                    .build();
        }
    }

    public static UserFormResponse from(UserFormQuery userFormQuery) {
        List<UserFormFieldListResponse> responses = userFormQuery.formFields().stream()
                .map(UserFormFieldListResponse::from)
                .toList();
        return UserFormResponse.builder()
                .clubName(userFormQuery.clubName())
                .title(userFormQuery.title())
                .description(userFormQuery.description())
                .applicationCount(userFormQuery.applicationCount())
                .startDate(userFormQuery.startDate())
                .endDate(userFormQuery.endDate())
                .formFields(responses)
                .build();
    }

}
