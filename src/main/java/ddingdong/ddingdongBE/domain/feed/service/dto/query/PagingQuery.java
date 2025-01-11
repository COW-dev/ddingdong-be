package ddingdong.ddingdongBE.domain.feed.service.dto.query;

public record PagingQuery(
    Long currentCursorId,
    Long nextCursorId,
    boolean hasNext
) {

    public static PagingQuery of(Long currentCursorId, Long nextCursorId, boolean hasNext) {
        return new PagingQuery(currentCursorId, nextCursorId, hasNext);
    }
}
