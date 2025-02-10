package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import java.time.LocalDate;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record MyFormApplicationPageQuery(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        boolean hasInterview,
        List<FormApplicationListQuery> formApplicationListQueries,
        PagingQuery pagingQuery
) {

    public static MyFormApplicationPageQuery of(Form form,
            List<FormApplicationListQuery> formApplicationListQueries, PagingQuery pagingQuery) {
        return new MyFormApplicationPageQuery(
                form.getTitle(),
                form.getStartDate(),
                form.getEndDate(),
                form.isHasInterview(),
                formApplicationListQueries,
                pagingQuery
        );
    }

    public static MyFormApplicationPageQuery createEmpty(Form form) {
        return new MyFormApplicationPageQuery(
                form.getTitle(), // title
                form.getStartDate(), // startDate
                form.getEndDate(), // endDate
                form.isHasInterview(), // hasInterview
                Collections.emptyList(), // formApplicationListQueries
                PagingQuery.createEmpty() // pagingQuery
        );
    }

    @Builder
    public record FormApplicationListQuery(
            Long id,
            Long formId,
            LocalDateTime submittedAt,
            String name,
            String studentNumber,
            String status
    ) {

        public static FormApplicationListQuery of(FormApplication formApplication) {
            return FormApplicationListQuery.builder()
                    .id(formApplication.getId())
                    .formId(formApplication.getForm().getId())
                    .submittedAt(formApplication.getCreatedAt())
                    .name(formApplication.getName())
                    .studentNumber(formApplication.getStudentNumber())
                    .status(formApplication.getStatus().toString())
                    .build();
        }
    }
}
