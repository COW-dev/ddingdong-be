package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralFeedRankingServiceTest extends TestContainerSupport {

    @Autowired
    private FeedRankingService feedRankingService;

    @Autowired
    private FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 각 월의 1위 동아리가 반환된다")
    @Test
    void getMonthlyWinners_success() {
        // given
        int year = 2025;
        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.createWinner(1L, "동아리A", year, 1),
                FeedMonthlyRankingFixture.createWinner(2L, "동아리B", year, 2),
                FeedMonthlyRankingFixture.createWinner(3L, "동아리C", year, 3)
        ));

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(3);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("동아리A");
            softly.assertThat(result.get(0).targetMonth()).isEqualTo(1);
            softly.assertThat(result.get(1).clubName()).isEqualTo("동아리B");
            softly.assertThat(result.get(1).targetMonth()).isEqualTo(2);
            softly.assertThat(result.get(2).clubName()).isEqualTo("동아리C");
            softly.assertThat(result.get(2).targetMonth()).isEqualTo(3);
        });
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 동점으로 같은 월에 ranking=1이 여러 개면 모두 반환된다")
    @Test
    void getMonthlyWinners_tieBreak() {
        // given
        int year = 2025;
        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.create(1L, "동아리A", 10, 100, 50, 20, year, 1, 1),
                FeedMonthlyRankingFixture.create(2L, "동아리B", 10, 100, 50, 20, year, 1, 1)
        ));

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).targetMonth()).isEqualTo(1);
            softly.assertThat(result.get(0).clubName()).isEqualTo("동아리A");
            softly.assertThat(result.get(1).targetMonth()).isEqualTo(1);
            softly.assertThat(result.get(1).clubName()).isEqualTo("동아리B");
        });
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 해당 연도 데이터가 없으면 빈 리스트를 반환한다")
    @Test
    void getMonthlyWinners_emptyData() {
        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(2025);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 2위 이하 동아리는 포함되지 않는다")
    @Test
    void getMonthlyWinners_excludesNonWinners() {
        // given
        int year = 2025;
        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.createWinner(1L, "1위 동아리", year, 1),
                FeedMonthlyRankingFixture.createWithRanking(2L, "2위 동아리", year, 1, 2),
                FeedMonthlyRankingFixture.createWithRanking(3L, "3위 동아리", year, 1, 3)
        ));

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).clubName()).isEqualTo("1위 동아리");
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 다른 연도의 1위 데이터는 포함되지 않는다")
    @Test
    void getMonthlyWinners_yearIsolation() {
        // given
        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.createWinner(1L, "2025년 동아리", 2025, 1),
                FeedMonthlyRankingFixture.createWinner(2L, "2024년 동아리", 2024, 1),
                FeedMonthlyRankingFixture.createWinner(3L, "2025년 3월 동아리", 2025, 3)
        ));

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(2025);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("2025년 동아리");
            softly.assertThat(result.get(0).targetMonth()).isEqualTo(1);
            softly.assertThat(result.get(1).clubName()).isEqualTo("2025년 3월 동아리");
            softly.assertThat(result.get(1).targetMonth()).isEqualTo(3);
        });
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 응답에 score 필드가 올바르게 매핑된다")
    @Test
    void getMonthlyWinners_scoreMapping() {
        // given
        int year = 2025;
        // feedCount=10, viewCount=100, likeCount=50, commentCount=20
        // score = 10*10 + 100*1 + 50*3 + 20*5 = 450
        feedMonthlyRankingRepository.save(
                FeedMonthlyRankingFixture.create(1L, "점수 검증 동아리", 10, 100, 50, 20, year, 1, 1));

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(1);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("점수 검증 동아리");
            softly.assertThat(result.get(0).score()).isEqualTo(450L);
            softly.assertThat(result.get(0).feedCount()).isEqualTo(10);
            softly.assertThat(result.get(0).viewCount()).isEqualTo(100);
            softly.assertThat(result.get(0).likeCount()).isEqualTo(50);
            softly.assertThat(result.get(0).commentCount()).isEqualTo(20);
        });
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 점수 높은 순서로 정렬된다")
    @Test
    void getClubFeedRanking_sortedByScore() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub("동아리A"));
        Club clubB = clubRepository.save(ClubFixture.createClub("동아리B"));

        // 동아리A: 피드 1개, viewCount=0, 좋아요 0, 댓글 0 → score = 1*10 = 10
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A"));

        // 동아리B: 피드 2개, viewCount=0, 좋아요 0, 댓글 0 → score = 2*10 = 20
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B1"));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B2"));

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        // when
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(year, month);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("동아리B");
            softly.assertThat(result.get(0).rank()).isEqualTo(1);
            softly.assertThat(result.get(0).score()).isEqualTo(20L);
            softly.assertThat(result.get(1).clubName()).isEqualTo("동아리A");
            softly.assertThat(result.get(1).rank()).isEqualTo(2);
            softly.assertThat(result.get(1).score()).isEqualTo(10L);
        });
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 동점자는 같은 순위를 부여받는다 (1-1-3)")
    @Test
    void getClubFeedRanking_tieRanking() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub("동아리A"));
        Club clubB = clubRepository.save(ClubFixture.createClub("동아리B"));
        Club clubC = clubRepository.save(ClubFixture.createClub("동아리C"));

        // 모두 피드 1개 → score = 10 (동점)
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A"));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B"));
        // 동아리C: 피드 2개 → score = 20
        feedRepository.save(FeedFixture.createImageFeed(clubC, "피드C1"));
        feedRepository.save(FeedFixture.createImageFeed(clubC, "피드C2"));

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        // when
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(year, month);

        // then
        assertThat(result).hasSize(3);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).rank()).isEqualTo(1);
            softly.assertThat(result.get(0).score()).isEqualTo(20L);
            // 동점자 2명 → 둘 다 2위
            softly.assertThat(result.get(1).rank()).isEqualTo(2);
            softly.assertThat(result.get(1).score()).isEqualTo(10L);
            softly.assertThat(result.get(2).rank()).isEqualTo(2);
            softly.assertThat(result.get(2).score()).isEqualTo(10L);
        });
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 피드가 없는 동아리도 score=0으로 포함된다")
    @Test
    void getClubFeedRanking_includesClubsWithNoFeeds() {
        // given
        Club clubA = clubRepository.save(ClubFixture.createClub("피드있는동아리"));
        clubRepository.save(ClubFixture.createClub("피드없는동아리"));

        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드"));

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        // when
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(year, month);

        // then
        assertThat(result).hasSize(2);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("피드있는동아리");
            softly.assertThat(result.get(0).score()).isEqualTo(10L);
            softly.assertThat(result.get(1).clubName()).isEqualTo("피드없는동아리");
            softly.assertThat(result.get(1).score()).isEqualTo(0L);
        });
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 해당 월에 피드가 없어도 동아리는 score=0으로 포함된다")
    @Test
    void getClubFeedRanking_noFeedsInMonth() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub("동아리"));
        feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        // when — 피드가 없는 연/월 조회 (동아리는 존재)
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(2000, 1);

        // then — 동아리는 포함되지만 score=0
        assertThat(result).hasSize(1);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("동아리");
            softly.assertThat(result.get(0).feedCount()).isEqualTo(0);
            softly.assertThat(result.get(0).score()).isEqualTo(0L);
        });
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 동아리가 없으면 빈 리스트를 반환한다")
    @Test
    void getClubFeedRanking_noClubs() {
        // when — 동아리 자체가 없는 상태
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(2025, 1);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 좋아요와 댓글이 점수에 반영된다")
    @Test
    void getClubFeedRanking_withLikesAndComments() {
        // given
        Club club = clubRepository.save(ClubFixture.createClub("활발한동아리"));
        Feed feed = feedRepository.save(FeedFixture.createImageFeed(club, "피드"));

        // 좋아요 2개
        feedLikeRepository.save(FeedFixture.createFeedLike(feed, "uuid-1"));
        feedLikeRepository.save(FeedFixture.createFeedLike(feed, "uuid-2"));

        // 댓글 1개
        feedCommentRepository.save(FeedFixture.createFeedComment(feed, "uuid-3", 1, "댓글"));

        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        // when
        List<ClubFeedRankingQuery> result = feedRankingService.getClubFeedRanking(year, month);

        // then
        // score = feedCount(1)*10 + viewCount(0)*1 + likeCount(2)*3 + commentCount(1)*5 = 10+0+6+5 = 21
        assertThat(result).hasSize(1);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).clubName()).isEqualTo("활발한동아리");
            softly.assertThat(result.get(0).feedCount()).isEqualTo(1);
            softly.assertThat(result.get(0).likeCount()).isEqualTo(2);
            softly.assertThat(result.get(0).commentCount()).isEqualTo(1);
            softly.assertThat(result.get(0).score()).isEqualTo(21L);
        });
    }
}
