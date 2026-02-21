package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.FeedException.CommentAccessDeniedException;
import ddingdong.ddingdongBE.common.exception.FeedException.CommentNotFoundException;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedCommentService implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedService feedService;
    private final ClubService clubService;

    @Override
    @Transactional
    public CreateFeedCommentQuery create(CreateFeedCommentCommand command) {
        Feed feed = feedService.getById(command.feedId());

        int anonymousNumber = feedCommentRepository
                .findAnonymousNumberByFeedIdAndUuid(command.feedId(), command.uuid())
                .orElseGet(() -> feedCommentRepository.findMaxAnonymousNumberByFeedId(command.feedId()) + 1);

        FeedComment comment = command.toEntity(feed, anonymousNumber);
        FeedComment saved = feedCommentRepository.save(comment);
        return new CreateFeedCommentQuery(saved.getId(), anonymousNumber);
    }

    @Override
    @Transactional
    public void delete(Long feedId, Long commentId, String uuid) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getFeed().getId().equals(feedId)) {
            throw new CommentNotFoundException();
        }
        if (!comment.getUuid().equals(uuid)) {
            throw new CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void forceDelete(User user, Long feedId, Long commentId) {
        Club club = clubService.getByUserId(user.getId());
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        Feed feed = comment.getFeed();
        if (!feed.getId().equals(feedId) || !feed.getClub().getId().equals(club.getId())) {
            throw new CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }

    @Override
    public List<FeedCommentQuery> getAllByFeedId(Long feedId) {
        return feedCommentRepository.findAllByFeedIdOrderByCreatedAtAsc(feedId)
                .stream()
                .map(FeedCommentQuery::from)
                .toList();
    }

    @Override
    public long countByFeedId(Long feedId) {
        return feedCommentRepository.countByFeedId(feedId);
    }
}
