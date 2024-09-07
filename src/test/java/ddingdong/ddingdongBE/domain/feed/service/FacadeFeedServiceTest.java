package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedListResponse;
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
            .set("score", Score.from(BigDecimal.ZERO))
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

  @DisplayName("모든 사용자는 전체 동아리의 최신 피드를 조회할 수 있다.")
  @Test
  void getNewestAll() {
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
    List<NewestFeedListResponse> responses = facadeFeedService.getNewestAll();
    // then
    assertThat(responses).hasSize(3);
    assertThat(responses.get(0).thumbnailUrl()).isEqualTo(feed6.getThumbnailUrl());
    assertThat(responses.get(0).id()).isEqualTo(feed6.getId());
    assertThat(responses.get(1).thumbnailUrl()).isEqualTo(feed4.getThumbnailUrl());
    assertThat(responses.get(1).id()).isEqualTo(feed4.getId());
    assertThat(responses.get(2).thumbnailUrl()).isEqualTo(feed2.getThumbnailUrl());
    assertThat(responses.get(2).id()).isEqualTo(feed2.getId());
  }
}
