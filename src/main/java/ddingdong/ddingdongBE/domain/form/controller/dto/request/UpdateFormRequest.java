package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormCommand.UpdateFormFieldCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record UpdateFormRequest(
        @Schema(description = "폼지 제목", example = "폼지제목입니다")
        @NotNull(message = "폼지 제목은 null이 될 수 없습니다.")
        String title,

        @Schema(description = "폼지 설명", example = "우리 동아리는 띵동입니다.")
        String description,

        @Schema(description = "폼지 시작일자", example = "2001-01-01")
        @NotNull(message = "폼지 시작일자는 null이 될 수 없습니다.")
        LocalDate startDate,

        @Schema(description = "폼지 종료일자", example = "2001-01-02")
        @NotNull(message = "폼지 종료일자는 null이 될 수 없습니다.")
        LocalDate endDate,

        @Schema(description = "면접여부", example = "true")
        @NotNull(message = "면접여부는 null이 될 수 없습니다.")
        boolean hasInterview,

        @Schema(description = "섹션 종류", example = "['공통', '서버']")
        @NotNull(message = "섹션 종류는 null이 될 수 없습니다.")
        List<String> sections,

        @ArraySchema(schema = @Schema(implementation = UpdateFormFieldRequest.class))
        List<UpdateFormFieldRequest> formFields
) {

    record UpdateFormFieldRequest(
            @Schema(description = "폼지 질문 식별자", example = "1")
            Long id,

            @Schema(description = "폼지 질문", example = "우리 동아리 들어올겁니까?")
            @NotNull(message = "질문는 null이 될 수 없습니다.")
            String question,

            @Schema(description = "질문 종류", example = "CHECK_BOX")
            @NotNull(message = "질문 종류는 null이 될 수 없습니다.")
            String type,

            @Schema(description = "질문의 선택리스트", example = "[지문1이다., 지문2이다., 지문3이다.]")
            List<String> options,

            @Schema(description = "필수여부", example = "true")
            @NotNull(message = "필수여부는 null이 될 수 없습니다.")
            boolean required,

            @Schema(description = "질문 순서", example = "1")
            @NotNull(message = "질문 순서는 null이 될 수 없습니다.")
            int order,

            @Schema(description = "질문 섹션 종류", example = "공통")
            @NotNull(message = "질문 섹션종류는 null이 될 수 없습니다.")
            String section
    ) {

        public UpdateFormFieldCommand toCommand() {
            return UpdateFormFieldCommand.builder()
                    .id(id)
                    .question(question)
                    .type(FieldType.findType(type))
                    .options(options)
                    .required(required)
                    .order(order)
                    .section(section)
                    .build();
        }
    }

    public UpdateFormCommand toCommand(User user, Long formId) {
        List<UpdateFormFieldCommand> updateFormFieldCommands = formFields.stream()
                .map(UpdateFormFieldRequest::toCommand)
                .toList();
        return UpdateFormCommand.builder()
                .user(user)
                .formId(formId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .hasInterview(hasInterview)
                .sections(sections)
                .formFieldCommands(updateFormFieldCommands)
                .build();
    }

}
