package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeedRankingSchedulerTest extends TestContainerSupport {

    @Autowired
    private FeedRankingScheduler feedRankingScheduler;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("createMonthlyRankingSnapshot으로 전월 스냅샷이 생성된다")
    @Test
    void createMonthlyRankingSnapshot_CreatesSnapshotForLastMonth() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub("테스트 동아리"));
        Feed feed1 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 1"));
        Feed feed2 = feedRepository.save(FeedFixture.createImageFeed(club, "피드 2"));
        entityManager.flush();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDateTime lastMonthDateTime = lastMonth.atStartOfDay().plusHours(12);
        entityManager.createNativeQuery("UPDATE feed SET created_at = :date WHERE club_id = :clubId")
                .setParameter("date", lastMonthDateTime)
                .setParameter("clubId", club.getId())
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();

        // when
        feedRankingScheduler.createMonthlyRankingSnapshot();

        // then
        List<FeedMonthlyRanking> rankings = feedMonthlyRankingRepository.findAll();
        assertThat(rankings).hasSize(1);

        FeedMonthlyRanking ranking = rankings.get(0);
        assertSoftly(softly -> {
            softly.assertThat(ranking.getClubName()).isEqualTo("테스트 동아리");
            softly.assertThat(ranking.getFeedCount()).isEqualTo(2);
            softly.assertThat(ranking.getTargetYear()).isEqualTo(lastMonth.getYear());
            softly.assertThat(ranking.getTargetMonth()).isEqualTo(lastMonth.getMonthValue());
        });
    }

    @DisplayName("이미 스냅샷이 존재하면 중복 생성하지 않는다")
    @Test
    void createMonthlyRankingSnapshot_SkipsWhenAlreadyExists() {
        // given
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        feedMonthlyRankingRepository.save(FeedMonthlyRanking.builder()
                .clubId(1L)
                .clubName("기존 동아리")
                .feedCount(3)
                .viewCount(50)
                .likeCount(10)
                .commentCount(5)
                .score(100)
                .targetYear(lastMonth.getYear())
                .targetMonth(lastMonth.getMonthValue())
                .build());

        Club club = clubRepository.save(ClubFixture.createClub("새 동아리"));
        feedRepository.save(FeedFixture.createImageFeed(club, "피드"));
        entityManager.flush();

        LocalDateTime lastMonthDateTime = lastMonth.atStartOfDay().plusHours(12);
        entityManager.createNativeQuery("UPDATE feed SET created_at = :date WHERE club_id = :clubId")
                .setParameter("date", lastMonthDateTime)
                .setParameter("clubId", club.getId())
                .executeUpdate();
        entityManager.flush();
        entityManager.clear();

        // when
        feedRankingScheduler.createMonthlyRankingSnapshot();

        // then
        List<FeedMonthlyRanking> rankings = feedMonthlyRankingRepository.findAll();
        assertThat(rankings).hasSize(1);
        assertThat(rankings.get(0).getClubName()).isEqualTo("기존 동아리");
    }

    @DisplayName("피드가 없으면 스냅샷을 생성하지 않는다")
    @Test
    void createMonthlyRankingSnapshot_NoSnapshotWhenNoFeeds() {
        // when
        feedRankingScheduler.createMonthlyRankingSnapshot();

        // then
        List<FeedMonthlyRanking> rankings = feedMonthlyRankingRepository.findAll();
        assertThat(rankings).isEmpty();
    }
}
