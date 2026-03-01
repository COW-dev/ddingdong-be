package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedMonthlyRankingRepository extends JpaRepository<FeedMonthlyRanking, Long> {

    boolean existsByTargetYearAndTargetMonth(int targetYear, int targetMonth);

    List<FeedMonthlyRanking> findAllByTargetYearAndTargetMonthAndRanking(
            int targetYear, int targetMonth, int ranking);

    List<FeedMonthlyRanking> findAllByTargetYearAndTargetMonthOrderByRankingAsc(
            int targetYear, int targetMonth);

    Optional<FeedMonthlyRanking> findByClubIdAndTargetYearAndTargetMonth(
            Long clubId, int targetYear, int targetMonth);
}
