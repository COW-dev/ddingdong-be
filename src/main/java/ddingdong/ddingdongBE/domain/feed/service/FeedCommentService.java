package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;

public interface FeedCommentService {

    CreateFeedCommentQuery create(CreateFeedCommentCommand command);

    void delete(Long feedId, Long commentId, String uuid);

    void forceDelete(User user, Long feedId, Long commentId);

    List<FeedCommentQuery> getAllByFeedId(Long feedId);

    long countByFeedId(Long feedId);
}
