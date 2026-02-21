package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;

public interface FeedRankingService {

    FeedRankingWinnerQuery getYearlyWinner(int year);
}
