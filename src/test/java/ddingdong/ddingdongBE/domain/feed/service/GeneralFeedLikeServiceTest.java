package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.FeedException.DuplicatedFeedLikeException;
import ddingdong.ddingdongBE.common.exception.FeedException.FeedLikeNotFoundException;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralFeedLikeServiceTest extends TestContainerSupport {

    @Autowired
    private FeedLikeService feedLikeService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private ClubRepository clubRepository;

    @DisplayName("피드 좋아요 생성 - 성공: 좋아요가 저장된다.")
    @Test
    void create_success() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));

        // when
        feedLikeService.create(feed.getId(), uuid);

        // then
        long count = feedLikeRepository.countByFeedId(feed.getId());
        assertThat(count).isEqualTo(1L);
    }

    @DisplayName("피드 좋아요 생성 - 실패: 이미 좋아요한 피드에 좋아요하면 예외가 발생한다.")
    @Test
    void create_fail_duplicate() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        feedLikeService.create(feed.getId(), uuid);

        // when & then
        assertThatThrownBy(() -> feedLikeService.create(feed.getId(), uuid))
                .isInstanceOf(DuplicatedFeedLikeException.class);
    }

    @DisplayName("피드 좋아요 취소 - 성공: 좋아요가 삭제된다.")
    @Test
    void delete_success() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        feedLikeService.create(feed.getId(), uuid);

        // when
        feedLikeService.delete(feed.getId(), uuid);

        // then
        long count = feedLikeRepository.countByFeedId(feed.getId());
        assertThat(count).isEqualTo(0L);
    }

    @DisplayName("피드 좋아요 취소 - 실패: 좋아요 기록이 없으면 예외가 발생한다.")
    @Test
    void delete_fail_notFound() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));

        // when & then
        assertThatThrownBy(() -> feedLikeService.delete(feed.getId(), uuid))
                .isInstanceOf(FeedLikeNotFoundException.class);
    }

    @DisplayName("피드 좋아요 여부 조회 - 성공: 좋아요한 피드는 true를 반환한다.")
    @Test
    void existsByFeedIdAndUuid_true() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));
        feedLikeService.create(feed.getId(), uuid);

        // when
        boolean exists = feedLikeService.existsByFeedIdAndUuid(feed.getId(), uuid);

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("피드 좋아요 여부 조회 - 성공: 좋아요하지 않은 피드는 false를 반환한다.")
    @Test
    void existsByFeedIdAndUuid_false() {
        // given
        String uuid = UUID.randomUUID().toString();
        Club club = clubRepository.save(ClubFixture.createClub());
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "활동 내용"));

        // when
        boolean exists = feedLikeService.existsByFeedIdAndUuid(feed.getId(), uuid);

        // then
        assertThat(exists).isFalse();
    }
}
