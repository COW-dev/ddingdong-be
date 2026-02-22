package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
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
}
