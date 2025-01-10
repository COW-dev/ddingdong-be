package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface FeedService {

    Slice<Feed> getFeedPageByClubId(Long clubId, int size, Long currentCursorId);

    List<Feed> getNewestAll();

    Feed getById(Long feedId);

    Long create(Feed feed);

    void delete(Feed feed);
}
