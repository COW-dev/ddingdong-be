package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FeedMonthlyRankingRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @DisplayName("existsByTargetYearAndTargetMonth로 스냅샷 존재 여부를 확인한다")
    @Test
    void existsByTargetYearAndTargetMonth() {
        // given
        feedMonthlyRankingRepository.save(
                FeedMonthlyRankingFixture.createWinner(1L, "테스트 동아리", 2026, 1));

        // when & then
        assertThat(feedMonthlyRankingRepository.existsByTargetYearAndTargetMonth(2026, 1)).isTrue();
        assertThat(feedMonthlyRankingRepository.existsByTargetYearAndTargetMonth(2026, 2)).isFalse();
    }

    @DisplayName("findByTargetYearAndRankingOrderByTargetMonthAsc - 해당 연도의 지정 순위만 월 오름차순으로 조회한다")
    @Test
    void findByTargetYearAndRankingOrderByTargetMonthAsc() {
        // given
        feedMonthlyRankingRepository.saveAll(List.of(
                FeedMonthlyRankingFixture.createWinner(1L, "1월 1위", 2025, 1),
                FeedMonthlyRankingFixture.createWithRanking(2L, "1월 2위", 2025, 1, 2),
                FeedMonthlyRankingFixture.createWinner(3L, "3월 1위", 2025, 3),
                FeedMonthlyRankingFixture.createWinner(4L, "2월 1위", 2025, 2),
                FeedMonthlyRankingFixture.createWinner(5L, "다른 연도", 2024, 1)
        ));

        // when
        List<FeedMonthlyRanking> result =
                feedMonthlyRankingRepository.findByTargetYearAndRankingOrderByTargetMonthAsc(2025, 1);

        // then
        assertThat(result).hasSize(3);
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).getClubName()).isEqualTo("1월 1위");
            softly.assertThat(result.get(0).getTargetMonth()).isEqualTo(1);
            softly.assertThat(result.get(1).getClubName()).isEqualTo("2월 1위");
            softly.assertThat(result.get(1).getTargetMonth()).isEqualTo(2);
            softly.assertThat(result.get(2).getClubName()).isEqualTo("3월 1위");
            softly.assertThat(result.get(2).getTargetMonth()).isEqualTo(3);
        });
    }

    @DisplayName("findByTargetYearAndRankingOrderByTargetMonthAsc - 해당 연도에 데이터가 없으면 빈 리스트를 반환한다")
    @Test
    void findByTargetYearAndRankingOrderByTargetMonthAsc_emptyResult() {
        // given
        feedMonthlyRankingRepository.save(
                FeedMonthlyRankingFixture.createWinner(1L, "2024년 동아리", 2024, 1));

        // when
        List<FeedMonthlyRanking> result =
                feedMonthlyRankingRepository.findByTargetYearAndRankingOrderByTargetMonthAsc(2025, 1);

        // then
        assertThat(result).isEmpty();
    }
}
