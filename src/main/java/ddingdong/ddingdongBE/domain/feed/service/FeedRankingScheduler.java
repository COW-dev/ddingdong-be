package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedRankingScheduler {

    private final FeedRepository feedRepository;
    private final FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Scheduled(cron = "0 0 3 1 * *")
    @Transactional
    public void createMonthlyRankingSnapshot() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int targetYear = lastMonth.getYear();
        int targetMonth = lastMonth.getMonthValue();

        if (feedMonthlyRankingRepository.existsByTargetYearAndTargetMonth(targetYear, targetMonth)) {
            log.info("피드 월별 랭킹 스냅샷이 이미 존재합니다. year={}, month={}", targetYear, targetMonth);
            return;
        }

        List<MonthlyFeedRankingDto> rankings = feedRepository.findMonthlyRankingByClub(targetYear, targetMonth);

        List<FeedMonthlyRanking> snapshots = rankings.stream()
                .map(dto -> FeedMonthlyRanking.builder()
                        .clubId(dto.getClubId())
                        .clubName(dto.getClubName())
                        .feedCount(dto.getFeedCount())
                        .viewCount(dto.getViewCount())
                        .likeCount(dto.getLikeCount())
                        .commentCount(dto.getCommentCount())
                        .score(dto.getScore())
                        .targetYear(targetYear)
                        .targetMonth(targetMonth)
                        .build())
                .toList();

        feedMonthlyRankingRepository.saveAll(snapshots);
        log.info("피드 월별 랭킹 스냅샷 생성 완료. year={}, month={}, count={}", targetYear, targetMonth, snapshots.size());
    }
}
