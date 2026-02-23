package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.List;

public interface FeedRankingService {

    List<FeedRankingWinnerQuery> getMonthlyWinners(int year);

    List<ClubFeedRankingQuery> getClubFeedRanking(int year, int month);

    ClubMonthlyStatusQuery getClubMonthlyStatus(Long userId, int year, int month);
}
