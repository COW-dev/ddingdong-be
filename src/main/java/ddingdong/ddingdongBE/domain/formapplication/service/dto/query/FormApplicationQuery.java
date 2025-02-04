package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.form.service.dto.query.FormQuery.FormFieldListQuery;

import lombok.Builder;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record FormApplicationQuery (
        LocalDateTime createdAt,
        String name,
        String studentNumber,
        String department,
        FormApplicationStatus status,
        List<FormFieldAnswerListQuery> formFieldAnswers
) {
    @Builder
    public record FormAnswerListQuery (
            Long fieldId,
            List<String> value
    ) {
        public static FormAnswerListQuery from(FormAnswer formAnswer) {
            return FormAnswerListQuery.builder()
                    .fieldId(formAnswer.getFormField().getId())
                    .value(formAnswer.getValue())
                    .build();
        }
    }
    @Builder
    public record FormFieldAnswerListQuery (
            Long fieldId,
            String question,
            FieldType type,
            List<String> options,
            Boolean required,
            Integer order,
            String section,
            List<String> value
    ) {
        public static FormFieldAnswerListQuery from(FormFieldListQuery formFieldListQuery, FormAnswerListQuery formAnswerListQuery) {
            return FormFieldAnswerListQuery.builder()
                    .fieldId(formFieldListQuery.id())
                    .question(formFieldListQuery.question())
                    .type(formFieldListQuery.type())
                    .options(formFieldListQuery.options())
                    .required(formFieldListQuery.required())
                    .order(formFieldListQuery.order())
                    .section(formFieldListQuery.section())
                    .value(formAnswerListQuery.value())
                    .build();
        }
    }
    public static FormApplicationQuery of(FormApplication formApplication, List<FormField> formFields, List<FormAnswer> formAnswers) {
        List<FormFieldListQuery> formFieldListQueries = formFields.stream()
                .map(FormFieldListQuery::from)
                .toList();
        List<FormAnswerListQuery> formAnswerListQueries = formAnswers.stream()
                .map(FormAnswerListQuery::from)
                .toList();
        Map<Long, FormAnswerListQuery> answerMap = formAnswerListQueries.stream()
                .collect(Collectors.toMap(FormAnswerListQuery::fieldId, Function.identity()));
        List<FormFieldAnswerListQuery> formFieldAnswerListQueries = formFieldListQueries.stream()
                .map(fieldQuery -> {
                    FormAnswerListQuery answerQuery = answerMap.get(fieldQuery.id());
                    if (answerQuery == null) {
                        answerQuery = FormAnswerListQuery.builder()
                                .fieldId(fieldQuery.id())
                                .value(null)
                                .build();
                    }
                    return FormFieldAnswerListQuery.from(fieldQuery, answerQuery);
                })
                .collect(Collectors.toList());
        return FormApplicationQuery.builder()
                .createdAt(formApplication.getCreatedAt())
                .name(formApplication.getName())
                .studentNumber(formApplication.getStudentNumber())
                .department(formApplication.getDepartment())
                .status(formApplication.getStatus())
                .formFieldAnswers(formFieldAnswerListQueries)
                .build();
    }
}
