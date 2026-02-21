package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeFeedCommentServiceImpl implements FacadeFeedCommentService {

    private final FeedCommentService feedCommentService;
    private final FeedService feedService;

    @Override
    @Transactional
    public CreateFeedCommentQuery create(CreateFeedCommentCommand command) {
        Feed feed = feedService.getById(command.feedId());
        return feedCommentService.create(command, feed);
    }

    @Override
    @Transactional
    public void delete(Long feedId, Long commentId, String uuid) {
        feedCommentService.delete(commentId, uuid);
    }
}
