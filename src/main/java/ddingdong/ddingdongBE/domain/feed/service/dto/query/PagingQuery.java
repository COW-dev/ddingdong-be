package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import org.springframework.data.domain.Slice;

public record PagingQuery(
    Long currentCursorId,
    Long nextCursorId,
    boolean hasNext
) {

    public static PagingQuery from(Slice<?> slice, Long nextCursorId) {
        Long currentCursorId = ((long) slice.getNumber() * slice.getSize());
        return new PagingQuery(currentCursorId, nextCursorId, slice.hasNext());
    }

}
