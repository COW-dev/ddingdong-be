package ddingdong.ddingdongBE.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
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

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 각 월의 1위 동아리가 반환된다.")
    @Test
    void getMonthlyWinners_success() {
        // given
        int year = 2025;
        FeedMonthlyRanking jan = createAndSaveRanking(1L, "동아리A", 10, 100, 50, 20, year, 1, 1);
        FeedMonthlyRanking feb = createAndSaveRanking(2L, "동아리B", 8, 80, 40, 15, year, 2, 1);
        FeedMonthlyRanking mar = createAndSaveRanking(3L, "동아리C", 12, 120, 60, 25, year, 3, 1);

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

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 동점으로 같은 월에 ranking=1이 여러 개면 첫 번째만 반환된다.")
    @Test
    void getMonthlyWinners_tieBreak() {
        // given
        int year = 2025;
        createAndSaveRanking(1L, "동아리A", 10, 100, 50, 20, year, 1, 1);
        createAndSaveRanking(2L, "동아리B", 10, 100, 50, 20, year, 1, 1);

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).targetMonth()).isEqualTo(1);
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 해당 연도 데이터가 없으면 빈 리스트를 반환한다.")
    @Test
    void getMonthlyWinners_emptyData() {
        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(2025);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("월별 1위 동아리 목록 조회 - 성공: 2위 이하 동아리는 포함되지 않는다.")
    @Test
    void getMonthlyWinners_excludesNonWinners() {
        // given
        int year = 2025;
        createAndSaveRanking(1L, "1위 동아리", 10, 100, 50, 20, year, 1, 1);
        createAndSaveRanking(2L, "2위 동아리", 5, 50, 25, 10, year, 1, 2);
        createAndSaveRanking(3L, "3위 동아리", 3, 30, 15, 5, year, 1, 3);

        // when
        List<FeedRankingWinnerQuery> result = feedRankingService.getMonthlyWinners(year);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).clubName()).isEqualTo("1위 동아리");
    }

    private FeedMonthlyRanking createAndSaveRanking(Long clubId, String clubName,
            long feedCount, long viewCount, long likeCount, long commentCount,
            int targetYear, int targetMonth, int ranking) {
        FeedMonthlyRanking entity = FeedMonthlyRanking.builder()
                .clubId(clubId)
                .clubName(clubName)
                .feedCount(feedCount)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .targetYear(targetYear)
                .targetMonth(targetMonth)
                .build();
        entity.assignRanking(ranking);
        return feedMonthlyRankingRepository.save(entity);
    }
}
