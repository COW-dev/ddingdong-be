package ddingdong.ddingdongBE.domain.formapplicaion.service.dto;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplicaion.entity.FormApplication;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateFormApplicationCommand(
        Form form,
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
        public FormAnswer toEntity(FormApplication formApplication, FormField formField) {
            return FormAnswer.builder()
                    .value(value)
                    .valueType(valueType)
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
                .form(form)
                .build();
    }

}
