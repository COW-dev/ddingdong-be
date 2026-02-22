package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;

@SpringBootTest
class GeneralFeedServiceTest extends TestContainerSupport {

    @Autowired
    private FeedService feedService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("주입된 Feed를 사용하여 feedRepository에 저장한 뒤, 해당 entity id를 반환한다.")
    @Test
    void create() {
        // given
        Feed feed = FeedFixture.createImageFeed(null, "활동내용");

        // when
        Long feedId = feedService.create(feed);

        // then
        Feed found = feedRepository.findById(feedId).orElseThrow();
        assertThat(found.getId()).isEqualTo(feedId);
        assertThat(found.getActivityContent()).isEqualTo("활동내용");
    }

    @DisplayName("입력한 파라미터의 정보에 따라 해당하는 페이지의 정보를 반환한다.")
    @Test
    void getFeedPageByClubId() {
        // given
        Club savedClub = clubRepository.save(ClubFixture.createClub("카우"));

        feedRepository.save(FeedFixture.createImageFeed(savedClub, "내용1"));
        feedRepository.save(FeedFixture.createVideoFeed(savedClub, "내용2"));
        feedRepository.save(FeedFixture.createImageFeed(savedClub, "내용3"));
        feedRepository.save(FeedFixture.createImageFeed(savedClub, "내용4"));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = -1L;

        // when
        Slice<Feed> page = feedService.getFeedPageByClubId(clubId, size, cursorId);

        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getNumber()).isZero();
    }

    @DisplayName("incrementViewCount로 피드 조회수가 증가한다")
    @Test
    void incrementViewCount() {
        // given
        Club savedClub = clubRepository.save(ClubFixture.createClub());
        Feed savedFeed = feedRepository.save(FeedFixture.createImageFeed(savedClub, "내용"));
        assertThat(savedFeed.getViewCount()).isZero();

        // when
        feedService.incrementViewCount(savedFeed.getId());
        entityManager.flush();
        entityManager.clear();

        // then
        Feed found = feedRepository.findById(savedFeed.getId()).orElseThrow();
        assertThat(found.getViewCount()).isEqualTo(1);
    }
}
