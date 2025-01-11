package ddingdong.ddingdongBE.domain.feed.repository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FeedRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("동아리 ID로 해당 동아리의 모든 피드를 최신순으로 조회한다.")
    @Test
    void findAllByClubIdOrderById() {
        // given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("name", "카우")
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("clubMembers", null)
                .sample();
        Club savedClub = clubRepository.save(club);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
                .set("club", savedClub)
                .set("activityContent", "내용1")
                .set("feedType", FeedType.IMAGE)
                .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
                .set("club", savedClub)
                .set("activityContent", "내용2")
                .set("feedType", FeedType.VIDEO)
                .sample();
        Feed feed3 = fixture.giveMeBuilder(Feed.class)
                .set("club", savedClub)
                .set("activityContent", "내용3")
                .set("feedType", FeedType.IMAGE)
                .sample();
        feedRepository.save(feed1);
        feedRepository.save(feed2);
        feedRepository.save(feed3);

        // when
        List<Feed> feeds = feedRepository.findAllByClubIdOrderById(savedClub.getId());

        // then
        Assertions.assertThat(feeds.get(0).getActivityContent()).isEqualTo("내용3");
        Assertions.assertThat(feeds.get(0).getId()).isEqualTo(3L);
        Assertions.assertThat(feeds.get(1).getActivityContent()).isEqualTo("내용2");
        Assertions.assertThat(feeds.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(feeds.get(2).getActivityContent()).isEqualTo("내용1");
        Assertions.assertThat(feeds.get(2).getId()).isEqualTo(1L);
    }
}
