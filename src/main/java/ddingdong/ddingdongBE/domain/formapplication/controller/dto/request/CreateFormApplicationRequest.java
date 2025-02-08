package ddingdong.ddingdongBE.domain.formapplication.controller.dto.request;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.CreateFormApplicationCommand.CreateFormAnswerCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateFormApplicationRequest(

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
      List<String> value
  ) {

    public CreateFormAnswerCommand toCommand() {
      return CreateFormAnswerCommand.builder()
          .fieldId(fieldId)
          .value(value)
          .build();
    }
  }

  public CreateFormApplicationCommand toCommand(Long formId) {
    List<CreateFormAnswerCommand> createFormAnswerCommands = formAnswers.stream()
        .map(CreateFormAnswerRequest::toCommand)
        .toList();
    return CreateFormApplicationCommand.builder()
        .formId(formId)
        .name(name)
        .studentNumber(studentNumber)
        .department(department)
        .status(FormApplicationStatus.SUBMITTED)
        .formAnswerCommands(createFormAnswerCommands)
        .build();
  }

}


