package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.banner.service.FacadeRankingBannerService;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import java.time.LocalDate;
import java.util.Comparator;
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
    private final FacadeRankingBannerService facadeRankingBannerService;

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
                        .targetYear(targetYear)
                        .targetMonth(targetMonth)
                        .build())
                .sorted(Comparator.comparingLong(FeedMonthlyRanking::getScore).reversed())
                .toList();

        assignRankings(snapshots);
        feedMonthlyRankingRepository.saveAll(snapshots);
        log.info("피드 월별 랭킹 스냅샷 생성 완료. year={}, month={}, count={}", targetYear, targetMonth, snapshots.size());

        createRankingBanners(targetYear, targetMonth);
    }

    private void createRankingBanners(int targetYear, int targetMonth) {
        List<FeedMonthlyRanking> firstPlaceRankings =
                feedMonthlyRankingRepository.findAllByTargetYearAndTargetMonthAndRanking(
                        targetYear, targetMonth, 1);

        if (firstPlaceRankings.isEmpty()) {
            log.info("피드 랭킹 1위 동아리가 없어 배너를 생성하지 않습니다. year={}, month={}", targetYear, targetMonth);
            return;
        }

        facadeRankingBannerService.createRankingBanners(firstPlaceRankings);
        log.info("피드 랭킹 1위 배너 생성 완료. year={}, month={}, count={}",
                targetYear, targetMonth, firstPlaceRankings.size());
    }

    private void assignRankings(List<FeedMonthlyRanking> snapshots) {
        int rank = 1;
        for (int i = 0; i < snapshots.size(); i++) {
            if (i > 0 && snapshots.get(i).getScore() < snapshots.get(i - 1).getScore()) {
                rank = i + 1;
            }
            snapshots.get(i).assignRanking(rank);
        }
    }
}
