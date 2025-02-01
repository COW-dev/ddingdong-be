package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.entity.FormResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateFormResponseCommand(
        Form form,
        LocalDateTime submittedAt,
        String name,
        String studentNumber,
        String department,
        List<CreateFormAnswerCommand> formAnswerCommands
) {
    @Builder
    public record CreateFormAnswerCommand(
            Long fieldId,
            String value,
            String valueType
    ) {
        public FormAnswer toEntity(FormResponse formResponse, FormField formField) {
            return FormAnswer.builder()
                    .value(value)
                    .valueType(valueType)
                    .formField(formField)
                    .build();
        }
    }

    public FormResponse toEntity(Form form) {
        return FormResponse.builder()
                .submittedAt(submittedAt)
                .name(name)
                .studentNumber(studentNumber)
                .department(department)
                .form(form)
                .build();
    }

}
