package ddingdong.ddingdongBE.domain.formapplication.service.dto.command;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateFormApplicationCommand(
        Long formId,
        String name,
        String studentNumber,
        String department,
        FormApplicationStatus status,
        List<CreateFormAnswerCommand> formAnswerCommands
) {
    @Builder
    public record CreateFormAnswerCommand(
            Long fieldId,
            List<String> value
    ) {
        public FormAnswer toEntity(FormApplication formApplication, FormField formField) {
            return FormAnswer.builder()
                    .value(value)
                    .formField(formField)
                    .formApplication(formApplication)
                    .build();
        }
    }

    public FormApplication toEntity(Form form) {
        return FormApplication.builder()
                .name(name)
                .studentNumber(studentNumber)
                .department(department)
                .status(status)
                .form(form)
                .build();
    }

}
