package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class FeedRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @BeforeEach
    void setUp() {
        feedRepository.deleteAll();
        feedRepository.flush();
    }

    @DisplayName("모든 동아리의 최신 피드를 모두 조회할 수 있다.")
    @Test
    void test() {
        // given
        Club club1 = fixture.giveMeBuilder(Club.class)
            .set("name", "카우1")
            .set("user", null)
            .set("score", Score.from(BigDecimal.ZERO))
            .set("clubMembers", null)
            .sample();
        Club club2 = fixture.giveMeBuilder(Club.class)
            .set("name", "카우2")
            .set("user", null)
            .set("score", Score.from(BigDecimal.ZERO))
            .set("clubMembers", null)
            .sample();
        Club club3 = fixture.giveMeBuilder(Club.class)
            .set("name", "카우3")
            .set("user", null)
            .set("score", Score.from(BigDecimal.ZERO))
            .set("clubMembers", null)
            .sample();
        Club savedClub1 = clubRepository.save(club1);
        Club savedClub2 = clubRepository.save(club2);
        Club savedClub3 = clubRepository.save(club3);

        Feed feed1 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub1)
            .set("activityContent", "내용 1 올드")
            .sample();
        Feed feed2 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub1)
            .set("activityContent", "내용 1 최신")
            .sample();
        Feed feed3 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub2)
            .set("activityContent", "내용 2 올드")
            .sample();
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub2)
            .set("activityContent", "내용 2 최신")
            .sample();
        Feed feed5 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub3)
            .set("activityContent", "내용 3 올드")
            .sample();
        Feed feed6 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub3)
            .set("activityContent", "내용 3 최신")
            .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4, feed5, feed6));

        // when
        List<Feed> newestFeeds = feedRepository.findNewestAll();

        // then
        assertThat(newestFeeds.size()).isEqualTo(3);
        assertThat(newestFeeds.get(0).getId()).isEqualTo(6L);
        assertThat(newestFeeds.get(1).getId()).isEqualTo(4L);
        assertThat(newestFeeds.get(2).getId()).isEqualTo(2L);
    }

    @DisplayName("size 개수보다 남은 feed의 개수가 적다면, 그 수만큼 페이지로 반환한다.")
    @Test
    void 페이지네이션_남은_개수가_사이즈보다_적은경우() {
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
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용4")
            .set("feedType", FeedType.IMAGE)
            .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = 2L;
        // when
        Slice<Feed> page = feedRepository.findPageByClubIdOrderById(clubId, size, cursorId);
        // then
        List<Feed> feeds = page.getContent();
        assertThat(feeds.size()).isEqualTo(1);
        assertThat(feeds.get(0).getId()).isEqualTo(feed1.getId());
        assertThat(feeds.get(0).getActivityContent()).isEqualTo(feed1.getActivityContent());

    }

    @DisplayName("cursorId보다 작은 Feed를 size 개수만큼 페이지로 반환한다.")
    @Test
    void findPageByClubIdOrderById() {
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
        Feed feed4 = fixture.giveMeBuilder(Feed.class)
            .set("club", savedClub)
            .set("activityContent", "내용4")
            .set("feedType", FeedType.IMAGE)
            .sample();
        feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4));

        Long clubId = savedClub.getId();
        int size = 2;
        Long cursorId = 4L;
        // when
        Slice<Feed> page = feedRepository.findPageByClubIdOrderById(clubId, size, cursorId);
        // then
        List<Feed> feeds = page.getContent();
        assertThat(feeds.size()).isEqualTo(2);
        assertThat(feeds.get(0).getId()).isEqualTo(feed3.getId());
        assertThat(feeds.get(0).getActivityContent()).isEqualTo(feed3.getActivityContent());
        assertThat(feeds.get(1).getId()).isEqualTo(feed2.getId());
        assertThat(feeds.get(1).getActivityContent()).isEqualTo(feed2.getActivityContent());
    }
}
