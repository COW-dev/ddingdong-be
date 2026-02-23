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
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedCountDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfig.class)
class FeedLikeRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubRepository clubRepository;

    @DisplayName("countsByFeedIds로 피드별 좋아요 수를 벌크 조회한다")
    @Test
    void countsByFeedIds_ReturnsCorrectCounts() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 1"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 2"));
        Feed feed3 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 3"));

        feedLikeRepository.save(FeedFixture.createFeedLike(feed1, "uuid-1"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feed1, "uuid-2"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feed1, "uuid-3"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feed2, "uuid-4"));

        List<Long> feedIds = List.of(feed1.getId(), feed2.getId(), feed3.getId());

        // when
        List<FeedCountDto> result = feedLikeRepository.countsByFeedIds(feedIds);

        // then
        Map<Long, Long> countMap = result.stream()
                .collect(Collectors.toMap(FeedCountDto::getFeedId, FeedCountDto::getCnt));

        assertSoftly(softly -> {
            softly.assertThat(countMap.getOrDefault(feed1.getId(), 0L)).isEqualTo(3);
            softly.assertThat(countMap.getOrDefault(feed2.getId(), 0L)).isEqualTo(1);
            softly.assertThat(countMap.containsKey(feed3.getId())).isFalse();
        });
    }

    @DisplayName("countsByFeedIds에 좋아요 없는 피드만 있으면 빈 리스트를 반환한다")
    @Test
    void countsByFeedIds_ReturnsEmptyWhenNoLikes() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        // when
        List<FeedCountDto> result = feedLikeRepository.countsByFeedIds(List.of(feed.getId()));

        // then
        assertThat(result).isEmpty();
    }
}
