package ddingdong.ddingdongBE.domain.banner.service;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.entity.BannerType;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import ddingdong.ddingdongBE.domain.feed.repository.FeedMonthlyRankingRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FacadeRankingBannerServiceImpl implements FacadeRankingBannerService {

    private static final String RANKING_BANNER_DIRECTORY = "ranking-banner";
    private static final String IMAGE_CONTENT_TYPE = "image/png";

    private final BannerService bannerService;
    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;
    private final BannerImageGenerator bannerImageGenerator;
    private final FeedMonthlyRankingRepository feedMonthlyRankingRepository;

    @Override
    @Transactional
    public void createRankingBanners(List<FeedMonthlyRanking> firstPlaceRankings) {
        deleteExistingRankingBanners();

        for (FeedMonthlyRanking ranking : firstPlaceRankings) {
            createBannerForRanking(ranking);
        }
    }

    @Override
    @Transactional
    public void regenerateLatestRankingBanners() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        List<FeedMonthlyRanking> firstPlaceRankings =
                feedMonthlyRankingRepository.findAllByTargetYearAndTargetMonthAndRanking(
                        lastMonth.getYear(), lastMonth.getMonthValue(), 1);

        if (firstPlaceRankings.isEmpty()) {
            log.info("재생성할 랭킹 1위 동아리가 없습니다. year={}, month={}",
                    lastMonth.getYear(), lastMonth.getMonthValue());
            return;
        }

        createRankingBanners(firstPlaceRankings);
        log.info("랭킹 배너 재생성 완료. year={}, month={}, count={}",
                lastMonth.getYear(), lastMonth.getMonthValue(), firstPlaceRankings.size());
    }

    private void deleteExistingRankingBanners() {
        List<Banner> existingBanners = bannerService.getAllByBannerType(BannerType.FEED_RANKING);
        for (Banner banner : existingBanners) {
            fileMetaDataService.updateStatusToDelete(DomainType.BANNER_WEB_IMAGE, banner.getId());
            fileMetaDataService.updateStatusToDelete(DomainType.BANNER_MOBILE_IMAGE, banner.getId());
            bannerService.delete(banner.getId());
        }
    }

    private void createBannerForRanking(FeedMonthlyRanking ranking) {
        Club club = clubService.getById(ranking.getClubId());
        BufferedImage clubLogo = downloadClubLogo(club.getId());

        byte[] webImage = bannerImageGenerator.generateWebBannerImage(
                club.getName(), clubLogo, club.getCategory(), ranking.getTargetMonth());
        byte[] mobileImage = bannerImageGenerator.generateMobileBannerImage(
                club.getName(), clubLogo, club.getCategory(), ranking.getTargetMonth());

        String webKey = s3FileService.uploadBytes(webImage, IMAGE_CONTENT_TYPE, RANKING_BANNER_DIRECTORY);
        String mobileKey = s3FileService.uploadBytes(mobileImage, IMAGE_CONTENT_TYPE, RANKING_BANNER_DIRECTORY);

        Banner banner = Banner.builder()
                .bannerType(BannerType.FEED_RANKING)
                .link(club.getClubUrl())
                .build();
        Long savedBannerId = bannerService.save(banner);

        createCoupledFileMetaData(webKey, "ranking-banner-web.png", DomainType.BANNER_WEB_IMAGE, savedBannerId);
        createCoupledFileMetaData(mobileKey, "ranking-banner-mobile.png", DomainType.BANNER_MOBILE_IMAGE, savedBannerId);

        log.info("랭킹 배너 생성 완료. clubId={}, clubName={}, bannerId={}",
                club.getId(), club.getName(), savedBannerId);
    }

    private BufferedImage downloadClubLogo(Long clubId) {
        List<FileMetaData> profileFiles = fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(
                DomainType.CLUB_PROFILE, clubId);
        if (profileFiles.isEmpty()) {
            log.warn("동아리 프로필 이미지가 없습니다. clubId={}", clubId);
            return null;
        }

        FileMetaData profileMetaData = profileFiles.get(0);
        UploadedFileUrlQuery urlQuery = s3FileService.getUploadedFileUrl(profileMetaData.getFileKey());
        try {
            return ImageIO.read(URI.create(urlQuery.cdnUrl()).toURL());
        } catch (Exception e) {
            log.warn("동아리 로고 다운로드 실패. clubId={}: {}", clubId, e.getMessage());
            return null;
        }
    }

    private void createCoupledFileMetaData(String fileKey, String fileName, DomainType domainType, Long entityId) {
        UUID fileId = UuidCreator.getTimeOrderedEpoch();
        FileMetaData fileMetaData = FileMetaData.builder()
                .id(fileId)
                .fileKey(fileKey)
                .fileName(fileName)
                .domainType(domainType)
                .entityId(entityId)
                .fileStatus(FileStatus.COUPLED)
                .order(0)
                .build();
        fileMetaDataService.create(fileMetaData);
    }
}
