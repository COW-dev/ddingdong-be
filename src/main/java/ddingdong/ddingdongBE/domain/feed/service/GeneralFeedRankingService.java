package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
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

    private static final int FEED_WEIGHT = 10;
    private static final int VIEW_WEIGHT = 1;
    private static final int LIKE_WEIGHT = 3;
    private static final int COMMENT_WEIGHT = 5;

    private final FeedRepository feedRepository;
    private final ClubService clubService;

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
            long totalScore = calculateScore(sorted.get(i));
            if (i > 0 && totalScore < previousScore) {
                rank = i + 1;
            }
            result.add(toClubFeedRankingQuery(rank, sorted.get(i)));
            previousScore = totalScore;
        }

        return result;
    }

    @Override
    public ClubMonthlyStatusQuery getClubMonthlyStatus(Long userId, int year, int month) {
        Club club = clubService.getByUserId(userId);
        List<ClubFeedRankingQuery> rankings = getClubFeedRanking(year, month);
        int lastMonthRank = getLastMonthRank(club.getId(), year, month);

        return rankings.stream()
                .filter(rankingQuery -> rankingQuery.clubId().equals(club.getId()))
                .findFirst()
                .filter(rankingQuery -> rankingQuery.totalScore() > 0)
                .map(rankingQuery -> toMonthlyStatus(year, month, rankingQuery, lastMonthRank))
                .orElse(ClubMonthlyStatusQuery.createEmpty(year, month, lastMonthRank));
    }

    private ClubMonthlyStatusQuery toMonthlyStatus(int year, int month,
            ClubFeedRankingQuery ranking, int lastMonthRank) {
        return ClubMonthlyStatusQuery.of(year, month, ranking.rank(), lastMonthRank,
                ranking.feedScore(), ranking.viewScore(), ranking.likeScore(), ranking.commentScore());
    }

    private int getLastMonthRank(Long clubId, int year, int month) {
        int lastYear = month == 1 ? year - 1 : year;
        int lastMonth = month == 1 ? 12 : month - 1;

        return feedMonthlyRankingRepository
                .findByClubIdAndTargetYearAndTargetMonth(clubId, lastYear, lastMonth)
                .map(FeedMonthlyRanking::getRanking)
                .orElse(0);
    }

    private ClubFeedRankingQuery toClubFeedRankingQuery(int rank, MonthlyFeedRankingDto rawRanking) {
        long feedScore = rawRanking.getFeedCount() * FEED_WEIGHT;
        long viewScore = rawRanking.getViewCount() * VIEW_WEIGHT;
        long likeScore = rawRanking.getLikeCount() * LIKE_WEIGHT;
        long commentScore = rawRanking.getCommentCount() * COMMENT_WEIGHT;
        long totalScore = feedScore + viewScore + likeScore + commentScore;
        return ClubFeedRankingQuery.of(rank, rawRanking.getClubId(), rawRanking.getClubName(),
                feedScore, viewScore, likeScore, commentScore, totalScore);
    }

    private long calculateScore(MonthlyFeedRankingDto rawRanking) {
        return rawRanking.getFeedCount() * FEED_WEIGHT
                + rawRanking.getViewCount() * VIEW_WEIGHT
                + rawRanking.getLikeCount() * LIKE_WEIGHT
                + rawRanking.getCommentCount() * COMMENT_WEIGHT;
    }
}
