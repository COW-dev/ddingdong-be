package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
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
}
