package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record FormApplicationQuery(
        boolean hasInterview,
        LocalDateTime createdAt,
        String name,
        String studentNumber,
        String department,
        String phoneNumber,
        String email,
        FormApplicationStatus status,
        String note,
        List<FormFieldAnswerListQuery> formFieldAnswers
) {

    @Builder
    public record FormFieldAnswerListQuery(
            Long fieldId,
            String question,
            FieldType type,
            List<String> options,
            Boolean required,
            Integer order,
            String section,
            List<String> value,
            List<FileQuery> fileQueries
    ) {

        public record FileQuery(
                String name,
                String cdnUrl
        ) {

        }

        public static FormFieldAnswerListQuery of(FormAnswer formAnswer, List<FileQuery> fieldQueries) {
            return FormFieldAnswerListQuery.builder()
                    .fieldId(formAnswer.getFormField().getId())
                    .question(formAnswer.getFormField().getQuestion())
                    .type(formAnswer.getFormField().getFieldType())
                    .options(formAnswer.getFormField().getOptions())
                    .required(formAnswer.getFormField().isRequired())
                    .order(formAnswer.getFormField().getFieldOrder())
                    .section(formAnswer.getFormField().getSection())
                    .value(formAnswer.getValue())
                    .fileQueries(fieldQueries)
                    .build();
        }
    }

    public static FormApplicationQuery of(Form form, FormApplication formApplication,
            List<FormFieldAnswerListQuery> formFieldAnswerListQueries) {
        return FormApplicationQuery.builder()
                .hasInterview(form.isHasInterview())
                .createdAt(formApplication.getCreatedAt())
                .name(formApplication.getName())
                .studentNumber(formApplication.getStudentNumber())
                .department(formApplication.getDepartment())
                .phoneNumber(formApplication.getPhoneNumber())
                .email(formApplication.getEmail())
                .status(formApplication.getStatus())
                .note(formApplication.getNote())
                .formFieldAnswers(formFieldAnswerListQueries)
                .build();
    }
}
