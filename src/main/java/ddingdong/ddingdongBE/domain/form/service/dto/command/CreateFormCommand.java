package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateFormCommand(
        User user,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        boolean hasInterview,
        List<CreateFormFieldCommand> formFieldCommands
) {

    @Builder
    public record CreateFormFieldCommand(
            String question,
            FieldType type,
            List<String> options,
            boolean required,
            int order,
            String section
    ) {

        public FormField toEntity(Form savedForm) {
            return FormField.builder()
                    .question(question)
                    .fieldType(type)
                    .options(options)
                    .required(required)
                    .fieldOrder(order)
                    .section(section)
                    .form(savedForm)
                    .build();
        }
    }

    public Form toEntity(Club club) {
        return Form.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .hasInterview(hasInterview)
                .club(club)
                .build();
    }
}
