package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralFeedRankingServiceTest extends TestContainerSupport {

    @Autowired
    private FeedRankingService feedRankingService;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("역대 1위 조회 - 성공: score가 가장 높은 동아리를 반환한다.")
    @Test
    void getYearlyWinner_returns_highest_score_club() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub());
        Club clubB = clubRepository.save(createClubWithName("음악 동아리"));

        Feed feedA = feedRepository.save(FeedFixture.createImageFeed(clubA, "A 활동"));
        Feed feedB = feedRepository.save(FeedFixture.createImageFeed(clubB, "B 활동"));

        // Club A: 3 likes -> score = feedCount(1)*10 + likeCount(3)*3 = 19
        feedLikeRepository.save(FeedFixture.createFeedLike(feedA, UUID.randomUUID().toString()));
        feedLikeRepository.save(FeedFixture.createFeedLike(feedA, UUID.randomUUID().toString()));
        feedLikeRepository.save(FeedFixture.createFeedLike(feedA, UUID.randomUUID().toString()));
        // Club B: 1 like -> score = feedCount(1)*10 + likeCount(1)*3 = 13
        feedLikeRepository.save(FeedFixture.createFeedLike(feedB, UUID.randomUUID().toString()));

        entityManager.flush();
        updateFeedCreatedAt(feedA.getId(), LocalDateTime.of(2025, 6, 15, 10, 0));
        updateFeedCreatedAt(feedB.getId(), LocalDateTime.of(2025, 6, 15, 10, 0));
        entityManager.clear();

        // when
        Optional<FeedRankingWinnerQuery> result = feedRankingService.getYearlyWinner(2025);

        // then
        assertThat(result).isPresent();
        FeedRankingWinnerQuery winner = result.get();
        assertThat(winner.clubName()).isEqualTo("컴퓨터공학과 동아리");
        assertThat(winner.feedCount()).isEqualTo(1L);
        assertThat(winner.viewCount()).isEqualTo(0L);
        assertThat(winner.likeCount()).isEqualTo(3L);
        assertThat(winner.score()).isEqualTo(19L);
        assertThat(winner.targetYear()).isEqualTo(2025);
        assertThat(winner.targetMonth()).isEqualTo(6);
    }

    @DisplayName("역대 1위 조회 - 성공: 동점 시 clubId가 작은 동아리를 반환한다.")
    @Test
    void getYearlyWinner_tiebreak_by_clubId_asc() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub());
        Club clubB = clubRepository.save(createClubWithName("음악 동아리"));

        Feed feedA = feedRepository.save(FeedFixture.createImageFeed(clubA, "A 활동"));
        Feed feedB = feedRepository.save(FeedFixture.createImageFeed(clubB, "B 활동"));

        // 동일 score: feedCount(1)*10 = 10
        entityManager.flush();
        updateFeedCreatedAt(feedA.getId(), LocalDateTime.of(2025, 3, 15, 10, 0));
        updateFeedCreatedAt(feedB.getId(), LocalDateTime.of(2025, 3, 15, 10, 0));
        entityManager.clear();

        // when
        Optional<FeedRankingWinnerQuery> result = feedRankingService.getYearlyWinner(2025);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().clubName()).isEqualTo("컴퓨터공학과 동아리");
    }

    @DisplayName("역대 1위 조회 - 성공: 현재 달 피드는 제외된다.")
    @Test
    void getYearlyWinner_excludes_current_month() {
        // given - 현재 달에만 피드 존재
        Club club = clubRepository.save(ClubFixture.createClub());
        feedRepository.save(FeedFixture.createImageFeed(club, "활동"));

        entityManager.flush();
        entityManager.clear();

        // when - 현재 연도 조회 시 현재 달 피드 제외
        int currentYear = LocalDateTime.now().getYear();
        Optional<FeedRankingWinnerQuery> result = feedRankingService.getYearlyWinner(currentYear);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("역대 1위 조회 - 성공: 피드가 없으면 빈 결과를 반환한다.")
    @Test
    void getYearlyWinner_returns_empty_when_no_feeds() {
        // when
        Optional<FeedRankingWinnerQuery> result = feedRankingService.getYearlyWinner(2025);

        // then
        assertThat(result).isEmpty();
    }

    private void updateFeedCreatedAt(Long feedId, LocalDateTime createdAt) {
        entityManager.createNativeQuery("UPDATE feed SET created_at = :createdAt WHERE id = :id")
                .setParameter("createdAt", createdAt)
                .setParameter("id", feedId)
                .executeUpdate();
    }

    private Club createClubWithName(String name) {
        return Club.builder()
                .clubMembers(new ArrayList<>())
                .name(name)
                .category("문화")
                .tag("음악, 밴드")
                .leader("이음악")
                .phoneNumber(PhoneNumber.from("010-9876-5432"))
                .location(Location.from("S4015"))
                .regularMeeting("매주 금요일 19:00")
                .introduction("음악을 좋아하는 학생들의 동아리")
                .activity("밴드 연습, 공연")
                .ideal("함께 음악하는 즐거움")
                .score(Score.from(BigDecimal.valueOf(80.0)))
                .build();
    }
}
