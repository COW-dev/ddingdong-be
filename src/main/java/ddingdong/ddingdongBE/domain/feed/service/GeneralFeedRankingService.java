package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedRankingService implements FeedRankingService {

    private static final int WINNER_RANKING = 1;

    private final FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Override
    public List<FeedRankingWinnerQuery> getMonthlyWinners(int year) {
        List<FeedMonthlyRanking> rankings =
                feedMonthlyRankingRepository.findByTargetYearAndRankingOrderByTargetMonthAsc(year, WINNER_RANKING);

        return rankings.stream()
                .map(FeedRankingWinnerQuery::from)
                .toList();
    }
}
