package ddingdong.ddingdongBE.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
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
        FeedMonthlyRanking ranking = FeedMonthlyRanking.builder()
                .clubId(1L)
                .clubName("테스트 동아리")
                .feedCount(5)
                .viewCount(100)
                .likeCount(20)
                .commentCount(10)
                .targetYear(2026)
                .targetMonth(1)
                .build();
        feedMonthlyRankingRepository.save(ranking);

        // when & then
        assertThat(feedMonthlyRankingRepository.existsByTargetYearAndTargetMonth(2026, 1)).isTrue();
        assertThat(feedMonthlyRankingRepository.existsByTargetYearAndTargetMonth(2026, 2)).isFalse();
    }
}
