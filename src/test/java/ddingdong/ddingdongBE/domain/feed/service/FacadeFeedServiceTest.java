package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeFeedServiceTest extends TestContainerSupport {

  @Autowired
  private ClubRepository clubRepository;

  @Autowired
  private FeedRepository feedRepository;

  @Autowired
  private FacadeFeedService facadeFeedService;

  private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

  @DisplayName("모든 사용자는 피드를 조회할 수 있다.")
  @Test
  void getAllFeed() {
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
    feedRepository.saveAll(List.of(feed1, feed2, feed3));
    // when
    List<FeedListResponse> responses = facadeFeedService.getAllByClubId(1L);
    // then
    assertThat(responses).hasSize(3);
    assertThat(responses)
        .extracting("id", "thumbnailUrl", "feedType")
        .containsExactly(
            tuple(3L, "썸네일3", "IMAGE"),
            tuple(2L, "썸네일2", "VIDEO"),
            tuple(1L, "썸네일1", "IMAGE")
        );
  }
}