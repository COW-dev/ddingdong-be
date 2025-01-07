package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;

public interface FeedService {

    List<Feed> getAllByClubId(Long clubId);

    List<Feed> getNewestAll();

    Feed getById(Long feedId);

    Long create(Feed feed);
}
