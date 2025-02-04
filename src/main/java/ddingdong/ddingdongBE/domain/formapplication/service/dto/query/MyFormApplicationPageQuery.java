package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

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
}
