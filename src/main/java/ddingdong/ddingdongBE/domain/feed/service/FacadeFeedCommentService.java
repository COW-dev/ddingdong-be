package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;

public interface FacadeFeedCommentService {

    CreateFeedCommentQuery create(CreateFeedCommentCommand command);

    void delete(Long feedId, Long commentId, String uuid);
}
