package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedRankingService implements FeedRankingService {

    private final FeedRepository feedRepository;

    @Override
    public Optional<FeedRankingWinnerQuery> getYearlyWinner(int year) {
        return feedRepository.findYearlyWinner(year)
                .map(FeedRankingWinnerQuery::from);
    }
}
