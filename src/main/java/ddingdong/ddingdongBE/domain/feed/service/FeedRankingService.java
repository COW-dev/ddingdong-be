package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import java.util.List;

public interface FeedRankingService {

    List<ClubFeedRankingQuery> getClubFeedRanking(int year, int month);

    ClubMonthlyStatusQuery getClubMonthlyStatus(Long userId, int year, int month);

    List<ClubFeedRankingQuery> getClubFeedRankingSnapshot(int year, int month);
}
