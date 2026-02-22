package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedRankingWinnerDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedRankingService implements FeedRankingService {

    private final FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Override
    public List<FeedRankingWinnerQuery> getMonthlyWinners(int year) {
        List<FeedRankingWinnerDto> winners = feedMonthlyRankingRepository.findMonthlyWinnersByYear(year);
        return winners.stream()
                .map(FeedRankingWinnerQuery::from)
                .toList();
    }
}
