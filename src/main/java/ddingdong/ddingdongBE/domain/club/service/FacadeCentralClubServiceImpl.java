package ddingdong.ddingdongBE.domain.club.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.FacadeFileMetaDataService;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralClubServiceImpl implements FacadeCentralClubService {

    private final ClubService clubService;
    private final FacadeFileMetaDataService facadeFileMetaDataService;
    private final S3FileService s3FileService;

    @Override
    public MyClubInfoQuery getMyClubInfo(Long userId) {
        Club club = clubService.getByUserId(userId);
        String clubProfileImageKey =
                facadeFileMetaDataService.getAllByEntityTypeAndEntityId(DomainType.CLUB_PROFILE, club.getId())
                        .stream()
                        .findFirst()
                        .map(FileMetaDataListQuery::key)
                        .orElse(null);
        String clubIntroductionImageKey =
                facadeFileMetaDataService.getAllByEntityTypeAndEntityId(DomainType.CLUB_INTRODUCTION, club.getId())
                        .stream()
                        .findFirst()
                        .map(FileMetaDataListQuery::key)
                        .orElse(null);

        UploadedFileUrlQuery profileImageUrlQuery = s3FileService.getUploadedFileUrl(clubProfileImageKey);
        UploadedFileUrlQuery introductionImageUrlQuery = s3FileService.getUploadedFileUrl(clubIntroductionImageKey);
        return MyClubInfoQuery.of(club, profileImageUrlQuery, introductionImageUrlQuery);
    }

    @Override
    @Transactional
    public Long updateClubInfo(UpdateClubInfoCommand command) {
        Club club = clubService.getByUserId(command.userId());
        clubService.update(club, command.toEntity());
        facadeFileMetaDataService.updateAll(
                new UpdateAllFileMetaDataCommand(
                        Stream.of(command.introductionImageId())
                                .filter(Objects::nonNull)
                                .toList(),
                        DomainType.CLUB_PROFILE,
                        club.getId())
        );
        facadeFileMetaDataService.updateAll(
                new UpdateAllFileMetaDataCommand(
                        Stream.of(command.introductionImageId())
                                .filter(Objects::nonNull)
                                .toList(),
                        DomainType.CLUB_INTRODUCTION,
                        club.getId())
        );
        return club.getId();
    }

}
