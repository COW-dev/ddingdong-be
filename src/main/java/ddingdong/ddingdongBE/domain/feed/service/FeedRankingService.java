package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.Optional;

public interface FeedRankingService {

    Optional<FeedRankingWinnerQuery> getYearlyWinner(int year);
}
