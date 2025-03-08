package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateFormCommand(
        User user,
        Long formId,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        boolean hasInterview,
        List<String> sections,
        List<UpdateFormFieldCommand> formFieldCommands
) {

    @Builder
    public record UpdateFormFieldCommand(
            Long id,
            String question,
            FieldType type,
            List<String> options,
            boolean required,
            int order,
            String section
    ) {

        public FormField toEntity(Form form) {
            return FormField.builder()
                    .id(id)
                    .form(form)
                    .question(question)
                    .fieldType(type)
                    .options(options)
                    .required(required)
                    .fieldOrder(order)
                    .section(section)
                    .build();
        }
    }

    public Form toEntity() {
        return Form.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .hasInterview(hasInterview)
                .sections(sections)
                .build();
    }
}
