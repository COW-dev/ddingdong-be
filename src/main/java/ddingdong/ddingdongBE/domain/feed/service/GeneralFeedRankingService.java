package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedRankingService implements FeedRankingService {

    private static final int WINNER_RANKING = 1;
    private static final int FEED_WEIGHT = 10;
    private static final int VIEW_WEIGHT = 1;
    private static final int LIKE_WEIGHT = 3;
    private static final int COMMENT_WEIGHT = 5;

    private final FeedMonthlyRankingRepository feedMonthlyRankingRepository;
    private final FeedRepository feedRepository;

    @Override
    public List<FeedRankingWinnerQuery> getMonthlyWinners(int year) {
        List<FeedMonthlyRanking> rankings =
                feedMonthlyRankingRepository.findByTargetYearAndRankingOrderByTargetMonthAsc(year, WINNER_RANKING);

        return rankings.stream()
                .map(FeedRankingWinnerQuery::from)
                .toList();
    }

    @Override
    public List<ClubFeedRankingQuery> getClubFeedRanking(int year, int month) {
        List<MonthlyFeedRankingDto> rawRankings = feedRepository.findClubFeedRankingRaw(year, month);

        List<MonthlyFeedRankingDto> sorted = rawRankings.stream()
                .sorted(Comparator.comparingLong(this::calculateScore).reversed())
                .toList();

        List<ClubFeedRankingQuery> result = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < sorted.size(); i++) {
            MonthlyFeedRankingDto dto = sorted.get(i);
            long score = calculateScore(dto);
            if (i > 0 && score < calculateScore(sorted.get(i - 1))) {
                rank = i + 1;
            }
            result.add(ClubFeedRankingQuery.of(rank, dto, score));
        }

        return result;
    }

    private long calculateScore(MonthlyFeedRankingDto dto) {
        return dto.getFeedCount() * FEED_WEIGHT
                + dto.getViewCount() * VIEW_WEIGHT
                + dto.getLikeCount() * LIKE_WEIGHT
                + dto.getCommentCount() * COMMENT_WEIGHT;
    }
}
