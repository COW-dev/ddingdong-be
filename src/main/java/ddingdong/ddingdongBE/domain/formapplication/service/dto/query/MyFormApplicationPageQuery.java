package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record MyFormApplicationPageQuery(
    List<FormApplicationListQuery> formApplicationListQueries,
    PagingQuery pagingQuery
) {

    public static MyFormApplicationPageQuery of(List<FormApplicationListQuery> formApplicationListQueries, PagingQuery pagingQuery) {
        return new MyFormApplicationPageQuery(formApplicationListQueries, pagingQuery);
    }

    public static MyFormApplicationPageQuery createEmpty() {
        return new MyFormApplicationPageQuery(Collections.emptyList(), PagingQuery.createEmpty());
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
