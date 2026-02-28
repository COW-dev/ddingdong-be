package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.FeedException.CommentAccessDeniedException;
import ddingdong.ddingdongBE.common.exception.FeedException.CommentNotFoundException;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralFeedCommentServiceTest extends TestContainerSupport {

    @Autowired
    private FeedCommentService feedCommentService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private ClubRepository clubRepository;

    @DisplayName("댓글 생성 - 성공: 댓글이 저장되고 anonymousNumber가 1로 채번된다.")
    @Test
    void create_success() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        CreateFeedCommentCommand command = CreateFeedCommentCommand.builder()
                .uuid(uuid)
                .feedId(feed.getId())
                .content("테스트 댓글")
                .build();

        // when
        CreateFeedCommentQuery result = feedCommentService.create(command, feed);

        // then
        assertThat(result.commentId()).isNotNull();
        assertThat(result.anonymousNumber()).isEqualTo(1);
    }

    @DisplayName("댓글 생성 - 성공: 동일 UUID 재방문 시 기존 anonymousNumber를 재사용한다.")
    @Test
    void create_sameUuidReusesAnonymousNumber() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        CreateFeedCommentCommand firstCommand = CreateFeedCommentCommand.builder()
                .uuid(uuid)
                .feedId(feed.getId())
                .content("첫 번째 댓글")
                .build();
        CreateFeedCommentQuery firstResult = feedCommentService.create(firstCommand, feed);

        CreateFeedCommentCommand secondCommand = CreateFeedCommentCommand.builder()
                .uuid(uuid)
                .feedId(feed.getId())
                .content("두 번째 댓글")
                .build();

        // when
        CreateFeedCommentQuery secondResult = feedCommentService.create(secondCommand, feed);

        // then
        assertThat(secondResult.anonymousNumber()).isEqualTo(firstResult.anonymousNumber());
    }

    @DisplayName("댓글 삭제 - 성공: 본인 UUID로 삭제한다.")
    @Test
    void delete_success() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, uuid, 1, "삭제할 댓글"));

        // when
        feedCommentService.delete(comment.getId(), uuid);

        // then
        assertThat(feedCommentRepository.findById(comment.getId())).isEmpty();
    }

    @DisplayName("댓글 삭제 - 실패: 존재하지 않는 댓글이면 CommentNotFoundException이 발생한다.")
    @Test
    void delete_commentNotFound() {
        // given
        String uuid = UUID.randomUUID().toString();

        // when & then
        assertThatThrownBy(() -> feedCommentService.delete(999L, uuid))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @DisplayName("댓글 삭제 - 실패: 다른 UUID로 삭제 시도 시 CommentAccessDeniedException이 발생한다.")
    @Test
    void delete_accessDenied() {
        // given
        String ownerUuid = UUID.randomUUID().toString();
        String otherUuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, ownerUuid, 1, "삭제 대상 댓글"));

        // when & then
        assertThatThrownBy(() -> feedCommentService.delete(comment.getId(), otherUuid))
                .isInstanceOf(CommentAccessDeniedException.class);
    }

    @DisplayName("강제삭제 - 성공: 동아리 회장이 자기 피드의 댓글을 강제삭제한다.")
    @Test
    void forceDelete_success() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, uuid, 1, "강제삭제 대상"));

        // when
        feedCommentService.forceDelete(feed, club, comment.getId());

        // then
        assertThat(feedCommentRepository.findById(comment.getId())).isEmpty();
    }

    @DisplayName("강제삭제 - 실패: 다른 동아리 피드의 댓글 강제삭제 시 CommentAccessDeniedException이 발생한다.")
    @Test
    void forceDelete_accessDenied() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club ownerClub = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(ownerClub, "활동 내용"));
        FeedComment comment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, uuid, 1, "강제삭제 대상"));

        Club otherClub = clubRepository.save(ClubFixture.createClub());

        // when & then
        assertThatThrownBy(() -> feedCommentService.forceDelete(feed, otherClub, comment.getId()))
                .isInstanceOf(CommentAccessDeniedException.class);
    }
}
