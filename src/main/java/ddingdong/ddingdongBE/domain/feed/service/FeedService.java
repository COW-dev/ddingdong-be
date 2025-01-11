package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;
import java.util.Optional;

public interface FeedService {

    List<Feed> getAllByClubId(Long clubId);

    List<Feed> getNewestAll();

    Feed getById(Long feedId);

    Optional<Feed> findById(Long feedId);

    Long create(Feed feed);

    void delete(Feed feed);
}
