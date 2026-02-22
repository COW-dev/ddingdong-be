package ddingdong.ddingdongBE.domain.feed.repository;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedRankingWinnerDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedMonthlyRankingRepository extends JpaRepository<FeedMonthlyRanking, Long> {

    boolean existsByTargetYearAndTargetMonth(int targetYear, int targetMonth);

    @Query(value = """
            SELECT r.club_name AS clubName,
                   r.feed_count AS feedCount,
                   r.view_count AS viewCount,
                   r.like_count AS likeCount,
                   r.comment_count AS commentCount,
                   r.score AS score,
                   r.target_year AS targetYear,
                   r.target_month AS targetMonth
              FROM (
                  SELECT fmr.*,
                         ROW_NUMBER() OVER (PARTITION BY fmr.target_month ORDER BY fmr.score DESC) AS rn
                    FROM feed_monthly_ranking fmr
                   WHERE fmr.target_year = :year
              ) r
             WHERE r.rn = 1
             ORDER BY r.target_month
            """, nativeQuery = true)
    List<FeedRankingWinnerDto> findMonthlyWinnersByYear(@Param("year") int year);
}
