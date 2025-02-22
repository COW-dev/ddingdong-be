package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubListQuery;
import ddingdong.ddingdongBE.domain.club.service.dto.query.UserClubQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeUserClubServiceImpl implements FacadeUserClubService {

    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;
    private final FormService formService;

    @Override
    public List<UserClubListQuery> findAllWithRecruitTimeCheckPoint(LocalDate now) {
        return clubService.findAll().stream()
                .map(club -> {
                    List<Form> forms = formService.getAllByClub(club);
                    Form form = formService.findActiveForm(forms) != null
                            ? formService.findActiveForm(forms)
                            : formService.getNewestForm(forms);
                    return UserClubListQuery.of(club, checkRecruit(now, form).getText());
                })
                .toList();
    }

    @Override
    public UserClubQuery getClub(Long clubId) {
        Club club = clubService.getById(clubId);
        List<Form> forms = formService.getAllByClub(club);
        Form form = formService.findActiveForm(forms) != null
                ? formService.findActiveForm(forms)
                : formService.getNewestForm(forms);
        return UserClubQuery.of(
                club,
                form,
                getFileKey(DomainType.CLUB_PROFILE, clubId),
                getFileKey(DomainType.CLUB_INTRODUCTION, clubId)
        );
    }

    private RecruitmentStatus checkRecruit(LocalDate now, Form form) {
        if (form == null || form.getStartDate().isAfter(now)) {
            return BEFORE_RECRUIT;
        }
        return form.getEndDate().isAfter(now) || form.getEndDate().isEqual(now) ? RECRUITING : END_RECRUIT;
    }

    private UploadedFileUrlQuery getFileKey(DomainType domainType, Long clubId) {
        return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, clubId)
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
                .findFirst()
                .orElse(null);
    }

}
