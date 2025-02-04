package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;

import java.util.List;

public record PagingQuery(
    Long currentCursorId,
    Long nextCursorId,
    boolean hasNext
) {

    public static PagingQuery of(Long currentCursorId, List<FormApplication> completeFormApplicaitons, boolean hasNext) {
        if (completeFormApplicaitons.isEmpty()) {
            return new PagingQuery(currentCursorId, currentCursorId, false);
        }
        return new PagingQuery(currentCursorId, completeFormApplicaitons.get(completeFormApplicaitons.size() - 1).getId(), hasNext);
    }

    public static PagingQuery createEmpty() {
        return new PagingQuery(-1L, -1L, false);
    }
}
