package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.FeedException.CommentAccessDeniedException;
import ddingdong.ddingdongBE.common.exception.FeedException.CommentNotFoundException;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedCommentQuery;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedCommentService implements FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;

    @Override
    @Transactional
    public CreateFeedCommentQuery create(CreateFeedCommentCommand command, Feed feed) {
        int anonymousNumber = feedCommentRepository
                .findAnonymousNumberByFeedIdAndUuid(feed.getId(), command.uuid())
                .orElseGet(() -> feedCommentRepository.findMaxAnonymousNumberByFeedId(feed.getId()) + 1);

        FeedComment comment = command.toEntity(feed, anonymousNumber);
        FeedComment saved = feedCommentRepository.save(comment);
        return new CreateFeedCommentQuery(saved.getId(), anonymousNumber);
    }

    @Override
    @Transactional
    public void delete(Long commentId, String uuid) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!Objects.equals(comment.getUuid(), uuid)) {
            throw new CommentAccessDeniedException();
        }
        feedCommentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void forceDelete(Feed feed, Club club, Long commentId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!comment.getFeed().getId().equals(feed.getId()) || !feed.getClub().getId().equals(club.getId())) {
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

    @Override
    public List<FeedCountDto> countsByFeedIds(List<Long> feedIds) {
        return feedCommentRepository.countsByFeedIds(feedIds);
    }
}
