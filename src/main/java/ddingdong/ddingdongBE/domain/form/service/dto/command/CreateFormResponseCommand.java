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
        LocalDateTime submittedAt,
        String name,
        String studentNumber,
        String department,
        List<CreateFormAnswerCommand> formAnswerCommands
) {
    @Builder
    public record CreateFormAnswerCommand(
            FormField formField,
            String value,
            String valueType
    ) {
        public FormAnswer toEntity(FormResponse formResponse) {
            return FormAnswer.builder()
                    .formField(formField)
                    .value(value)
                    .valueType(valueType)
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
