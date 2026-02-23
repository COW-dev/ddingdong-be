package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.config.JpaAuditingConfig;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfig.class)
class FeedCommentRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubRepository clubRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("여러 피드의 댓글 수를 한 번에 조회한다")
    @Test
    void countsByFeedIds_ReturnsCorrectCounts() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 1"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 2"));

        feedCommentRepository.save(FeedFixture.createFeedComment(feed1, "uuid-1", 1, "댓글 1"));
        feedCommentRepository.save(FeedFixture.createFeedComment(feed1, "uuid-2", 2, "댓글 2"));
        feedCommentRepository.save(FeedFixture.createFeedComment(feed2, "uuid-3", 1, "댓글 3"));

        List<Long> feedIds = List.of(feed1.getId(), feed2.getId());

        // when
        List<FeedCountDto> result = feedCommentRepository.countsByFeedIds(feedIds);

        // then
        Map<Long, Long> countMap = result.stream()
                .collect(Collectors.toMap(FeedCountDto::getFeedId, FeedCountDto::getCnt));

        assertSoftly(softly -> {
            softly.assertThat(countMap.get(feed1.getId())).isEqualTo(2);
            softly.assertThat(countMap.get(feed2.getId())).isEqualTo(1);
        });
    }

    @DisplayName("삭제된 댓글은 댓글 수 집계에서 제외된다")
    @Test
    void countsByFeedIds_ExcludesSoftDeletedComments() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        feedCommentRepository.save(FeedFixture.createFeedComment(feed, "uuid-1", 1, "활성 댓글"));
        FeedComment deletedComment = feedCommentRepository.save(
                FeedFixture.createFeedComment(feed, "uuid-2", 2, "삭제 댓글"));

        feedCommentRepository.delete(deletedComment);
        entityManager.flush();
        entityManager.clear();

        // when
        List<FeedCountDto> result = feedCommentRepository.countsByFeedIds(List.of(feed.getId()));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCnt()).isEqualTo(1);
    }

    @DisplayName("댓글이 없는 피드는 집계 결과에 포함되지 않는다")
    @Test
    void countsByFeedIds_ReturnsEmptyWhenNoComments() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        // when
        List<FeedCountDto> result = feedCommentRepository.countsByFeedIds(List.of(feed.getId()));

        // then
        assertThat(result).isEmpty();
    }
}
