package ddingdong.ddingdongBE.domain.feed.repository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
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
        .sample();
    Club savedClub = clubRepository.save(club);
    Feed feed1 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub)
        .set("thumbnailUrl", "썸네일1")
        .set("feedType", FeedType.IMAGE)
        .sample();
    Feed feed2 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub)
        .set("thumbnailUrl", "썸네일2")
        .set("feedType", FeedType.VIDEO)
        .sample();
    Feed feed3 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub)
        .set("thumbnailUrl", "썸네일3")
        .set("feedType", FeedType.IMAGE)
        .sample();
    feedRepository.save(feed1);
    feedRepository.save(feed2);
    feedRepository.save(feed3);
    // when
    List<Feed> feeds = feedRepository.findAllByClubIdOrderById(savedClub.getId());
    // then
    Assertions.assertThat(feeds.get(0).getThumbnailUrl()).isEqualTo("썸네일3");
    Assertions.assertThat(feeds.get(0).getId()).isEqualTo(3L);
    Assertions.assertThat(feeds.get(1).getThumbnailUrl()).isEqualTo("썸네일2");
    Assertions.assertThat(feeds.get(1).getId()).isEqualTo(2L);
    Assertions.assertThat(feeds.get(2).getThumbnailUrl()).isEqualTo("썸네일1");
    Assertions.assertThat(feeds.get(2).getId()).isEqualTo(1L);
  }

  @DisplayName("모든 동아리의 최신 피드를 모두 조회할 수 있다.")
  @Test
  void test() {
    // given
    Club club1 = fixture.giveMeBuilder(Club.class)
        .set("name", "카우1")
        .set("user", null)
        .sample();
    Club club2 = fixture.giveMeBuilder(Club.class)
        .set("name", "카우2")
        .set("user", null)
        .sample();
    Club club3 = fixture.giveMeBuilder(Club.class)
        .set("name", "카우3")
        .set("user", null)
        .sample();
    Club savedClub1 = clubRepository.save(club1);
    Club savedClub2 = clubRepository.save(club2);
    Club savedClub3 = clubRepository.save(club3);

    Feed feed1 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub1)
        .set("thumbnailUrl", "클럽 1 올드 url")
        .sample();
    Feed feed2 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub1)
        .set("thumbnailUrl", "클럽 1 최신 url")
        .sample();
    Feed feed3 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub2)
        .set("thumbnailUrl", "클럽 2 올드 url")
        .sample();
    Feed feed4 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub2)
        .set("thumbnailUrl", "클럽 2 최신 url")
        .sample();
    Feed feed5 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub3)
        .set("thumbnailUrl", "클럽 3 올드 url")
        .sample();
    Feed feed6 = fixture.giveMeBuilder(Feed.class)
        .set("club", savedClub3)
        .set("thumbnailUrl", "클럽 3 최신 url")
        .sample();
    feedRepository.saveAll(List.of(feed1, feed2, feed3, feed4, feed5, feed6));
    // when
    List<Feed> newestFeeds = feedRepository.findNewestAll();
    // then
    Assertions.assertThat(newestFeeds.size()).isEqualTo(3);
    Assertions.assertThat(newestFeeds.get(0).getId()).isEqualTo(6L);
    Assertions.assertThat(newestFeeds.get(1).getId()).isEqualTo(4L);
    Assertions.assertThat(newestFeeds.get(2).getId()).isEqualTo(2L);
  }
}
