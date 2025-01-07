package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralFeedServiceTest extends TestContainerSupport {

    @Autowired
    private FeedService feedService;

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
}
