package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.PagingQuery;
import io.swagger.v3.oas.annotations.media.Schema;

public record PagingResponse(
    @Schema(name = "현재 커서 id", description = "9")
    Long currentCursorId,
    @Schema(name = "다음 커서 id", description = "10")
    Long nextCursorId,
    @Schema(name = "다음 커서 존재 여부", description = "true")
    boolean hasNext
) {

    public static PagingResponse from(PagingQuery pagingQuery) {
        return new PagingResponse(pagingQuery.currentCursorId(), pagingQuery.nextCursorId(), pagingQuery.hasNext());
    }
}
