package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import java.util.List;

public interface FeedCommentService {

    CreateFeedCommentQuery create(CreateFeedCommentCommand command, Feed feed);

    void delete(Long commentId, String uuid);

    void forceDelete(Feed feed, Club club, Long commentId);

    List<FeedCommentQuery> getAllByFeedId(Long feedId);

    long countByFeedId(Long feedId);
}
