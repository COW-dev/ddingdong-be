package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import org.springframework.data.domain.Slice;
import java.util.Optional;

public interface FeedService {

    Slice<Feed> getFeedPageByClubId(Long clubId, int size, Long currentCursorId);

    Slice<Feed> getNewestFeedPerClubPage(int size, Long currentCursorId);

    Feed getById(Long feedId);

    Optional<Feed> findById(Long feedId);

    Long create(Feed feed);

    void delete(Feed feed);
}
