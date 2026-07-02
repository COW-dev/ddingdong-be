package ddingdong.ddingdongBE.domain.banner.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.entity.BannerType;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacadeRankingBannerServiceImplTest {

    private static final int TARGET_YEAR = 2026;
    private static final int TARGET_MONTH = 6;

    @InjectMocks
    private FacadeRankingBannerServiceImpl facadeRankingBannerService;
    @Mock
    private BannerService bannerService;
    @Mock
    private ClubService clubService;
    @Mock
    private FileMetaDataService fileMetaDataService;
    @Mock
    private S3FileService s3FileService;
    @Mock
    private BannerImageGenerator bannerImageGenerator;
    @Mock
    private FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @DisplayName("피드 점수가 0인 1위 동아리만 있으면 배너를 생성하지 않고 기존 배너도 삭제하지 않는다")
    @Test
    void createRankingBanners_allZeroScore_generatesNothing() {
        FeedMonthlyRanking zeroRanking = FeedMonthlyRankingFixture.create(
                1L, "피드없는동아리", 0, 0, 0, 0, TARGET_YEAR, TARGET_MONTH, 1);

        facadeRankingBannerService.createRankingBanners(List.of(zeroRanking));

        verify(bannerService, never()).getAllByBannerType(any());
        verify(bannerService, never()).save(any());
        verify(bannerImageGenerator, never()).generateWebBannerImage(any(), any(), any(), anyInt());
    }

    @DisplayName("점수가 양수인 단독 1위는 배너를 1개 생성한다")
    @Test
    void createRankingBanners_singleWinner_generatesOneBanner() {
        FeedMonthlyRanking winner = FeedMonthlyRankingFixture.createWinner(
                1L, "우승동아리", TARGET_YEAR, TARGET_MONTH);
        stubCommonBannerCreation();
        stubClub(1L, "우승동아리");

        facadeRankingBannerService.createRankingBanners(List.of(winner));

        verify(bannerService, times(1)).save(any(Banner.class));
        verify(bannerImageGenerator, times(1)).generateWebBannerImage(any(), any(), any(), anyInt());
    }

    @DisplayName("점수가 양수인 공동 1위 2팀은 배너를 2개 생성한다")
    @Test
    void createRankingBanners_tiedWinners_generatesTwoBanners() {
        FeedMonthlyRanking firstWinner = FeedMonthlyRankingFixture.createWinner(
                1L, "동아리A", TARGET_YEAR, TARGET_MONTH);
        FeedMonthlyRanking secondWinner = FeedMonthlyRankingFixture.createWinner(
                2L, "동아리B", TARGET_YEAR, TARGET_MONTH);
        stubCommonBannerCreation();
        stubClub(1L, "동아리A");
        stubClub(2L, "동아리B");

        facadeRankingBannerService.createRankingBanners(List.of(firstWinner, secondWinner));

        verify(bannerService, times(2)).save(any(Banner.class));
        verify(bannerImageGenerator, times(2)).generateWebBannerImage(any(), any(), any(), anyInt());
    }

    @DisplayName("점수가 0인 동아리는 제외하고 점수가 양수인 동아리만 배너를 생성한다")
    @Test
    void createRankingBanners_mixedScore_generatesOnlyForPositiveScore() {
        FeedMonthlyRanking winner = FeedMonthlyRankingFixture.createWinner(
                1L, "피드있는동아리", TARGET_YEAR, TARGET_MONTH);
        FeedMonthlyRanking zeroRanking = FeedMonthlyRankingFixture.create(
                2L, "피드없는동아리", 0, 0, 0, 0, TARGET_YEAR, TARGET_MONTH, 1);
        stubCommonBannerCreation();
        stubClub(1L, "피드있는동아리");

        facadeRankingBannerService.createRankingBanners(List.of(winner, zeroRanking));

        verify(bannerService, times(1)).save(any(Banner.class));
        verify(bannerImageGenerator).generateWebBannerImage(eq("피드있는동아리"), any(), any(), anyInt());
    }

    private void stubClub(Long clubId, String clubName) {
        when(clubService.getById(clubId)).thenReturn(ClubFixture.createClub(clubName));
    }

    private void stubCommonBannerCreation() {
        when(fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(eq(DomainType.CLUB_PROFILE), any()))
                .thenReturn(List.of());
        when(bannerService.getAllByBannerType(BannerType.FEED_RANKING)).thenReturn(List.of());
        when(bannerImageGenerator.generateWebBannerImage(any(), any(), any(), anyInt()))
                .thenReturn(new byte[]{1});
        when(bannerImageGenerator.generateMobileBannerImage(any(), any(), any(), anyInt()))
                .thenReturn(new byte[]{1});
        when(s3FileService.uploadBytes(any(), any(), any())).thenReturn("file-key");
        when(bannerService.save(any(Banner.class))).thenReturn(1L);
    }
}
