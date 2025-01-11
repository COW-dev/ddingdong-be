package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import java.math.BigDecimal;
import java.util.List;
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

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("주입된 Feed를 사용하여 feedRepository에 저장한 뒤, 해당 entity id를 반환한다.")
    @Test
    void create() {
        // given
        Feed feed = fixtureMonkey.giveMeBuilder(Feed.class)
            .set("activityContent", "활동내용")
            .set("feedType", FeedType.IMAGE)
            .set("deletedAt", null)
            .set("club", null)
            .sample();
        // when
        Long feedId = feedService.create(feed);
        // then
        Feed finded = feedRepository.findById(feedId).orElse(null);
        assertThat(finded.getId()).isEqualTo(feedId);
        assertThat(finded.getActivityContent()).isEqualTo("활동내용");
        assertThat(finded.getFeedType()).isEqualTo(FeedType.IMAGE);
    }

    @DisplayName("입력한 파라미터의 정보에 따라 해당하는 페이지의 정보를 반환한다.")
    @Test
    void getFeedPageByClubId() {
        // given
        Club club = fixtureMonkey.giveMeBuilder(Club.class)
            .set("name", "카우")
            .set("user", null)
            .set("score", Score.from(BigDecimal.ZERO))
            .set("clubMembers", null)
            .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixtureMonkey.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용1")
            .set("feedType", FeedType.IMAGE)
            .sample();
        Feed feed2 = fixtureMonkey.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용2")
            .set("feedType", FeedType.VIDEO)
            .sample();
        Feed feed3 = fixtureMonkey.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용3")
            .set("feedType", FeedType.IMAGE)
            .sample();
        Feed feed4 = fixtureMonkey.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용4")
            .set("feedType", FeedType.IMAGE)
            .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = -1L;
        // when
        Slice<Feed> page = feedService.getFeedPageByClubId(clubId, size, cursorId);
        // then

        assertThat(page.getContent().size()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getContent().get(page.getContent().size() - 1).getId()).isEqualTo(3);
        assertThat(page.hasNext()).isTrue();
    }
}
