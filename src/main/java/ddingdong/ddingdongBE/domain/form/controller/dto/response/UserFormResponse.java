package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.UserFormQuery.UserFormFieldListQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record UserFormResponse(
        @Schema(description = "폼지 제목", example = "카우 1기 폼지")
        String title,
        @Schema(description = "폼지 설명", example = "폼지 설명입니다")
        String description,
        @ArraySchema(schema = @Schema(implementation = UserFormFieldListResponse.class))
        List<UserFormFieldListResponse> formFields
) {

    @Builder
    record UserFormFieldListResponse(
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
                .title(userFormQuery.title())
                .description(userFormQuery.description())
                .formFields(responses)
                .build();
    }

}
