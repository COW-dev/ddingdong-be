package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedMonthlyRankingRepository extends JpaRepository<FeedMonthlyRanking, Long> {

    boolean existsByTargetYearAndTargetMonth(int targetYear, int targetMonth);
}
