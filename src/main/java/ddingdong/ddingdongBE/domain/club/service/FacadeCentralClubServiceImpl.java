package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.CLUB_INTRODUCTION_IMAGE;
import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory.CLUB_PROFILE_IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import java.util.ArrayList;
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

    @Override
    public MyClubInfoQuery getMyClubInfo(Long userId) {
        Club club = clubService.getByUserId(userId);
        UploadedImageUrlQuery profileImageUrlQuery = s3FileService.getUploadedFileUrl(
            club.getProfileImageKey());
        UploadedImageUrlQuery introductionImageUrlQuery =
                s3FileService.getUploadedFileUrl(club.getIntroductionImageKey());
        return MyClubInfoQuery.of(club, profileImageUrlQuery, introductionImageUrlQuery);
    }

    @Override
    @Transactional
    public Long updateClubInfo(UpdateClubInfoCommand command) {
        Club club = clubService.getByUserId(command.userId());
        clubService.update(club, command.toEntity());
        createFileMetaData(command);
        return club.getId();
    }

    private void createFileMetaData(UpdateClubInfoCommand command) {
        List<FileMetaData> metaDataList = new ArrayList<>();
        if (command.profileImageKey() != null) {
            metaDataList.add(FileMetaData.of(command.profileImageKey(), CLUB_PROFILE_IMAGE));
        }
        if (command.introductionImageKey() != null) {
            metaDataList.add(FileMetaData.of(command.introductionImageKey(), CLUB_INTRODUCTION_IMAGE));
        }
        if (!metaDataList.isEmpty()) {
            fileMetaDataService.create(metaDataList);
        }
    }

}
