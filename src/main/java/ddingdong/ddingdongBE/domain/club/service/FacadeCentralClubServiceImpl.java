package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.service.FormService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralClubServiceImpl implements FacadeCentralClubService {

    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;
    private final FormService formService;

    @Override
    public MyClubInfoQuery getMyClubInfo(Long userId) {
        Club club = clubService.getByUserId(userId);
        List<Form> forms = formService.getAllByClub(club);
        Form form = formService.findActiveForm(forms) != null
                ? formService.findActiveForm(forms)
                : formService.getNewestForm(forms);
        return MyClubInfoQuery.of(
                club,
                form,
                getFileKey(DomainType.CLUB_PROFILE, club.getId()),
                getFileKey(DomainType.CLUB_INTRODUCTION, club.getId())
        );
    }

    @Override
    @Transactional
    public Long updateClubInfo(UpdateClubInfoCommand command) {
        Club club = clubService.getByUserId(command.userId());
        clubService.update(club, command.toEntity());
        fileMetaDataService.update(command.profileImageId(), DomainType.CLUB_PROFILE, club.getId());
        fileMetaDataService.update(command.introductionImageId(), DomainType.CLUB_INTRODUCTION, club.getId());
        return club.getId();
    }

    private UploadedFileUrlQuery getFileKey(DomainType domainType, Long clubId) {
        return fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, clubId)
                .stream()
                .map(fileMetaData -> s3FileService.getUploadedFileUrl(fileMetaData.getFileKey()))
                .findFirst()
                .orElse(null);
    }

}
