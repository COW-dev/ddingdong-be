package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.result.CreateFeedCommentResult;
import java.util.List;

public interface FeedCommentService {

    CreateFeedCommentResult create(CreateFeedCommentCommand command);

    void delete(Long commentId, String uuid);

    void forceDelete(Long feedId, Long commentId);

    List<FeedCommentQuery> getAllByFeedId(Long feedId);

    long countByFeedId(Long feedId);
}
