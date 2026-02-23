package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedCommentRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedLikeRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class GeneralFeedRankingServiceTest extends TestContainerSupport {

    @Autowired
    private FeedRankingService feedRankingService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedLikeRepository feedLikeRepository;

    @Autowired
    private FeedCommentRepository feedCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @DisplayName("동아리별 피드 랭킹 조회 - 성공: 동점자는 같은 순위를 부여받는다 (1-2-2)")
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

    @DisplayName("동아리 이달의 현황 조회 - 성공: 피드가 있으면 가중치 점수와 rank가 반환된다")
    @Test
    void getClubMonthlyStatus_withFeeds() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        Club club = clubRepository.save(ClubFixture.createClub(user));
        feedRepository.save(FeedFixture.createImageFeed(club, "피드1"));
        feedRepository.save(FeedFixture.createImageFeed(club, "피드2"));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(user.getId(), year, month);

        // then
        // feedScore = feedCount(2)*10 = 20, viewScore = 0, likeScore = 0, commentScore = 0
        assertSoftly(softly -> {
            softly.assertThat(result.year()).isEqualTo(year);
            softly.assertThat(result.month()).isEqualTo(month);
            softly.assertThat(result.rank()).isEqualTo(1);
            softly.assertThat(result.feedScore()).isEqualTo(20L);
            softly.assertThat(result.viewScore()).isEqualTo(0L);
            softly.assertThat(result.likeScore()).isEqualTo(0L);
            softly.assertThat(result.commentScore()).isEqualTo(0L);
            softly.assertThat(result.totalScore()).isEqualTo(20L);
            softly.assertThat(result.lastMonthRank()).isEqualTo(0);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 피드가 없으면 모든 값 0으로 반환된다")
    @Test
    void getClubMonthlyStatus_noFeeds() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        clubRepository.save(ClubFixture.createClub(user));

        // when — 피드가 없는 연/월
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(user.getId(), 2000, 1);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.year()).isEqualTo(2000);
            softly.assertThat(result.month()).isEqualTo(1);
            softly.assertThat(result.rank()).isEqualTo(0);
            softly.assertThat(result.lastMonthRank()).isEqualTo(0);
            softly.assertThat(result.feedScore()).isEqualTo(0L);
            softly.assertThat(result.viewScore()).isEqualTo(0L);
            softly.assertThat(result.likeScore()).isEqualTo(0L);
            softly.assertThat(result.commentScore()).isEqualTo(0L);
            softly.assertThat(result.totalScore()).isEqualTo(0L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 2개 동아리 중 내 동아리의 정확한 순위가 반환된다")
    @Test
    void getClubMonthlyStatus_rankAccuracy() {
        // given
        User userA = userRepository.save(UserFixture.createClubUser("clubA", "1234"));
        Club clubA = clubRepository.save(ClubFixture.createClub(userA));
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A-1"));
        feedRepository.save(FeedFixture.createImageFeed(clubA, "피드A-2"));

        User userB = userRepository.save(UserFixture.createClubUser("clubB", "1234"));
        Club clubB = clubRepository.save(ClubFixture.createClub(userB));
        feedRepository.save(FeedFixture.createImageFeed(clubB, "피드B"));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when — 동아리B 회장이 조회
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(userB.getId(), year, month);

        // then — 동아리A(totalScore=20) > 동아리B(totalScore=10), 동아리B는 rank=2
        assertSoftly(softly -> {
            softly.assertThat(result.rank()).isEqualTo(2);
            softly.assertThat(result.feedScore()).isEqualTo(10L);
            softly.assertThat(result.totalScore()).isEqualTo(10L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 저번 달에 피드가 있으면 lastMonthRank가 반환된다")
    @Test
    void getClubMonthlyStatus_withLastMonthRank() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        Club club = clubRepository.save(ClubFixture.createClub(user));

        // 저번 달 피드 생성
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        Feed lastMonthFeed = feedRepository.save(FeedFixture.createImageFeed(club, "저번달 피드"));
        jdbcTemplate.update("UPDATE feed SET created_at = ? WHERE id = ?",
                Timestamp.valueOf(lastMonth.atStartOfDay()), lastMonthFeed.getId());

        // 이번 달 피드 생성
        feedRepository.save(FeedFixture.createImageFeed(club, "이번달 피드"));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(user.getId(), year, month);

        // then — 저번 달에도 피드가 있으므로 lastMonthRank > 0
        assertSoftly(softly -> {
            softly.assertThat(result.rank()).isEqualTo(1);
            softly.assertThat(result.lastMonthRank()).isEqualTo(1);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 저번 달에 피드가 없으면 lastMonthRank는 0이다")
    @Test
    void getClubMonthlyStatus_noLastMonthFeeds() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        Club club = clubRepository.save(ClubFixture.createClub(user));

        // 이번 달 피드만 생성 (저번 달 피드 없음)
        feedRepository.save(FeedFixture.createImageFeed(club, "이번달 피드"));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(user.getId(), year, month);

        // then — 저번 달 피드가 없으므로 lastMonthRank = 0
        assertThat(result.lastMonthRank()).isEqualTo(0);
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 이번 달 피드가 없어도 저번 달 순위가 반환된다")
    @Test
    void getClubMonthlyStatus_noCurrentMonthFeed_butHasLastMonthRank() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        Club club = clubRepository.save(ClubFixture.createClub(user));

        // 저번 달 피드만 생성 (이번 달 피드 없음)
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        Feed lastMonthFeed = feedRepository.save(FeedFixture.createImageFeed(club, "저번달 피드"));
        jdbcTemplate.update("UPDATE feed SET created_at = ? WHERE id = ?",
                Timestamp.valueOf(lastMonth.atStartOfDay()), lastMonthFeed.getId());

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        // when
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(user.getId(), year, month);

        // then — 이번 달 score=0이어도 저번 달 순위가 정상 반환되어야 한다
        assertSoftly(softly -> {
            softly.assertThat(result.rank()).isEqualTo(0);
            softly.assertThat(result.lastMonthRank()).isEqualTo(1);
            softly.assertThat(result.totalScore()).isEqualTo(0L);
        });
    }

    @DisplayName("동아리 이달의 현황 조회 - 성공: 1월 조회 시 전년도 12월 순위가 반환된다")
    @Test
    void getClubMonthlyStatus_januaryLooksAtDecember() {
        // given
        User user = userRepository.save(UserFixture.createClubUser());
        Club club = clubRepository.save(ClubFixture.createClub(user));

        // 전년도 12월 피드 생성
        int currentYear = LocalDate.now().getYear();
        Feed decemberFeed = feedRepository.save(FeedFixture.createImageFeed(club, "12월 피드"));
        jdbcTemplate.update("UPDATE feed SET created_at = ? WHERE id = ?",
                Timestamp.valueOf(LocalDateTime.of(currentYear - 1, 12, 15, 10, 0)), decemberFeed.getId());

        // 1월 피드 생성
        Feed januaryFeed = feedRepository.save(FeedFixture.createImageFeed(club, "1월 피드"));
        jdbcTemplate.update("UPDATE feed SET created_at = ? WHERE id = ?",
                Timestamp.valueOf(LocalDateTime.of(currentYear, 1, 15, 10, 0)), januaryFeed.getId());

        // when — 1월 조회
        ClubMonthlyStatusQuery result = feedRankingService.getClubMonthlyStatus(
                user.getId(), currentYear, 1);

        // then — 전년도 12월 순위가 lastMonthRank로 반환된다
        assertSoftly(softly -> {
            softly.assertThat(result.rank()).isEqualTo(1);
            softly.assertThat(result.lastMonthRank()).isEqualTo(1);
        });
    }
}
