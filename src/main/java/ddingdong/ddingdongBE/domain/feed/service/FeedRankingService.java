package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.List;

public interface FeedRankingService {

    List<FeedRankingWinnerQuery> getMonthlyWinners(int year);
}
