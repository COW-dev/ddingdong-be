package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubMemberResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.UpdateClubCommand;
import ddingdong.ddingdongBE.file.controller.dto.response.FileUrlResponse;
import ddingdong.ddingdongBE.file.entity.FileCategory;
import ddingdong.ddingdongBE.file.service.FileMetaDataService;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.FileMetaDataCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeClubService {

    private final ClubService clubService;
    private final FileMetaDataService fileMetaDataService;
    private final S3FileService s3FileService;

    @Transactional
    public void updateClub(Long userId, UpdateClubCommand command) {
        String profileImageUrl = saveImageMetaDataAndFindUploadedUrl(
                command.profileImageFileMetaDataCommand(), FileCategory.CLUB_PROFILE_IMAGE);
        String introductionImageUrl = saveImageMetaDataAndFindUploadedUrl(
                command.introductionImageFileMetaDataCommand(), FileCategory.CLUB_INTRODUCTION_IMAGE);

        clubService.update(userId, command.toEntity(profileImageUrl, introductionImageUrl));
    }

    public ClubResponse getClub(Long clubId) {
        Club club = clubService.getByClubId(clubId);
        return buildClubResponse(club);
    }

    public ClubResponse getMyClub(Long userId) {
        Club club = clubService.getByUserId(userId);
        return buildClubResponse(club);
    }

    private ClubResponse buildClubResponse(Club club) {
        FileUrlResponse profileImageUrlResponse = fileMetaDataService.getFileUrlResponseByUrl(club.getProfileImageUrl());
        FileUrlResponse introductionImageUrlResponse = fileMetaDataService.getFileUrlResponseByUrl(club.getIntroductionImageUrl());
        List<ClubMemberResponse> clubMemberResponses = club.getClubMembers().stream()
                .map(ClubMemberResponse::from)
                .toList();

        return ClubResponse.of(club, profileImageUrlResponse, introductionImageUrlResponse, clubMemberResponses);
    }

    private String saveImageMetaDataAndFindUploadedUrl(FileMetaDataCommand fileMetaDataCommand, FileCategory category) {
        if (fileMetaDataCommand != null) {
            fileMetaDataService.save(fileMetaDataCommand.toEntity(category));
            return s3FileService.getUploadedFileUrl(fileMetaDataCommand.fileName(),
                    fileMetaDataCommand.fileId());
        }
        return null;
    }


}
