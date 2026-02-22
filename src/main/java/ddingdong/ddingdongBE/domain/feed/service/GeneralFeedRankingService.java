package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
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
    private final ClubService clubService;

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
        List<MonthlyFeedRankingDto> rawRankings = feedRepository.findMonthlyRankingByClub(year, month);

        List<MonthlyFeedRankingDto> sorted = rawRankings.stream()
                .sorted(Comparator.comparingLong(this::calculateScore).reversed())
                .toList();

        List<ClubFeedRankingQuery> result = new ArrayList<>();
        long previousScore = Long.MAX_VALUE;
        int rank = 1;
        for (int i = 0; i < sorted.size(); i++) {
            long score = calculateScore(sorted.get(i));
            if (i > 0 && score < previousScore) {
                rank = i + 1;
            }
            result.add(ClubFeedRankingQuery.of(rank, sorted.get(i), score));
            previousScore = score;
        }

        return result;
    }

    @Override
    public ClubMonthlyStatusQuery getClubMonthlyStatus(Long userId, int year, int month) {
        Club club = clubService.getByUserId(userId);
        return getClubMonthlyStatusByClubId(club.getId(), year, month);
    }

    @Override
    public ClubMonthlyStatusQuery getClubMonthlyStatusByClubId(Long clubId, int year, int month) {
        List<ClubFeedRankingQuery> rankings = getClubFeedRanking(year, month);

        return rankings.stream()
                .filter(rankingQuery -> rankingQuery.clubId().equals(clubId))
                .findFirst()
                .filter(rankingQuery -> rankingQuery.score() > 0)
                .map(rankingQuery -> ClubMonthlyStatusQuery.from(year, month, rankingQuery))
                .orElse(ClubMonthlyStatusQuery.createEmpty(year, month));
    }

    private long calculateScore(MonthlyFeedRankingDto dto) {
        return dto.getFeedCount() * FEED_WEIGHT
                + dto.getViewCount() * VIEW_WEIGHT
                + dto.getLikeCount() * LIKE_WEIGHT
                + dto.getCommentCount() * COMMENT_WEIGHT;
    }
}
