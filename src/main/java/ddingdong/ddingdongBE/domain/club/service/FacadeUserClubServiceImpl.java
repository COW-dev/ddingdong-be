package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
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
        return UserClubQuery.of(
                club,
                s3FileService.getUploadedFileUrl(club.getProfileImageKey()),
                s3FileService.getUploadedFileUrl(club.getIntroductionImageKey())
        );
    }

    private RecruitmentStatus checkRecruit(LocalDateTime now, Club club) {
        if (club.getStartRecruitPeriod() == null || club.getEndRecruitPeriod() == null
                || club.getStartRecruitPeriod().isAfter(now)) {
            return BEFORE_RECRUIT;
        }
        return club.getEndRecruitPeriod().isAfter(now) ? RECRUITING : END_RECRUIT;
    }

}
