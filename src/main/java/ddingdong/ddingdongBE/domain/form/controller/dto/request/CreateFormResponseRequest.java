package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormResponseCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.CreateFormResponseCommand.CreateFormAnswerCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateFormResponseRequest(
        @NotNull(message = "지원 일시는 null일 수 없습니다.")
        @Schema(description = "지원 일시", example = "2025-01-01T00:00")
        LocalDateTime submittedAt,

        @NotNull(message = "지원자 이름은 필수 입력 사항입니다.")
        @Schema(description = "지원자 이름", example = "김띵동")
        String name,

        @NotNull(message = "지원자 학번은 필수 입력 사항입니다.")
        @Schema(description = "학번", example = "60200000")
        String studentNumber,

        @NotNull(message = "지원자 학과는 필수 입력 사항입니다.")
        @Schema(description = "학과", example = "융합소프트웨어학부 응용소프트웨어전공")
        String department,

        @ArraySchema(schema = @Schema(implementation = CreateFormAnswerRequest.class))
        List<CreateFormAnswerRequest> formAnswers
) {
            record CreateFormAnswerRequest(
                    @NotNull(message = "질문 id는 null이 될 수 없습니다.")
                    @Schema(description = "질문 id", example = "1")
                    Long fieldId,

                    @Schema(description = "답변 값")
                    String value,

                    @NotNull(message = "질문 타입은 null이 될 수 없습니다.")
                    @Schema(description = "질문 타입", example = "RADIO")
                    String valueType

                    ) {
                public CreateFormAnswerCommand toCommand() {
                    return CreateFormAnswerCommand.builder()
                            .fieldId(fieldId)
                            .value(value)
                            .valueType(valueType)
                            .build();
                }
            }

            public CreateFormResponseCommand toCommand() {
                List<CreateFormAnswerCommand> createFormAnswerCommands = formAnswers.stream()
                        .map(CreateFormAnswerRequest::toCommand)
                        .toList();
                return CreateFormResponseCommand.builder()
                        .submittedAt(submittedAt)
                        .name(name)
                        .studentNumber(studentNumber)
                        .department(department)
                        .formAnswerCommands(createFormAnswerCommands)
                        .build();
            }

    }


