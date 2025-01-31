package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormCommand.CreateFormFieldCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


public record CreateFormRequest(
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

        @ArraySchema(schema = @Schema(implementation = CreateFormFieldRequest.class))
        List<CreateFormFieldRequest> formFields
) {

    record CreateFormFieldRequest(
            @Schema(description = "폼지 질문", example = "우리 동아리 들어올겁니까?")
            @NotNull(message = "질문는 null이 될 수 없습니다.")
            String question,

            @Schema(description = "질문 종류", example = "CHECK_BOX")
            @NotNull(message = "질문 종류는 null이 될 수 없습니다.")
            FieldType type,

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

        public CreateFormFieldCommand toCommand() {
            return CreateFormFieldCommand.builder()
                    .question(question)
                    .type(type)
                    .options(options)
                    .required(required)
                    .order(order)
                    .section(section)
                    .build();
        }
    }

    public CreateFormCommand toCommand(User user) {
        List<CreateFormFieldCommand> createFormFieldCommands = formFields.stream()
                .map(CreateFormFieldRequest::toCommand)
                .toList();
        return CreateFormCommand.builder()
                .user(user)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .hasInterview(hasInterview)
                .formFieldCommands(createFormFieldCommands)
                .build();
    }

}
