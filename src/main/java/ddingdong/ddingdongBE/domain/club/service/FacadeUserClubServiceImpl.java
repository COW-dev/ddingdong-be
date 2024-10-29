package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FacadeFileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeUserClubServiceImpl implements FacadeUserClubService {

    private final ClubService clubService;
    private final FacadeFileMetaDataService facadeFileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public List<UserClubListQuery> findAllWithRecruitTimeCheckPoint(LocalDateTime now) {
        return clubService.findAll().stream()
                .map(club -> UserClubListQuery.of(club, checkRecruit(now, club).getText()))
                .toList();
    }

    @Override
    public UserClubQuery getClub(Long clubId) {
        Club club = clubService.getById(clubId);
        String clubProfileImageKey = getFileKey(DomainType.CLUB_PROFILE, clubId);
        String clubIntroductionImageKey = getFileKey(DomainType.CLUB_INTRODUCTION, clubId);
        return UserClubQuery.of(
                club,
                s3FileService.getUploadedFileUrl(clubProfileImageKey),
                s3FileService.getUploadedFileUrl(clubIntroductionImageKey)
        );
    }

    private RecruitmentStatus checkRecruit(LocalDateTime now, Club club) {
        if (club.getStartRecruitPeriod() == null || club.getEndRecruitPeriod() == null
                || club.getStartRecruitPeriod().isAfter(now)) {
            return BEFORE_RECRUIT;
        }
        return club.getEndRecruitPeriod().isAfter(now) ? RECRUITING : END_RECRUIT;
    }

    private String getFileKey(DomainType domainType, Long clubId) {
        return facadeFileMetaDataService.getAllByEntityTypeAndEntityId(domainType, clubId)
                .stream()
                .map(FileMetaDataListQuery::key)
                .findFirst()
                .orElse(null);
    }

}
