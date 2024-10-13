package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.repository.FileInformationRepository;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.FileStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralClubServiceImpl implements FacadeCentralClubService {

    private final ClubService clubService;
    private final FileInformationService fileInformationService;
    private final FileStore fileStore;
    private final FileInformationRepository fileInformationRepository;

    @Override
    public MyClubInfoQuery getMyClubInfo(Long userId) {
        Club club = clubService.getByUserId(userId);

        List<String> profileImageUrl = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId());
        List<String> introduceImageUrls = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + club.getId());
        return MyClubInfoQuery.of(club, profileImageUrl, introduceImageUrls);
    }

    @Override
    @Transactional
    public Long updateClubInfo(UpdateClubInfoCommand command) {
        Club club = clubService.getByUserId(command.userId());
        updateIntroduceImageInformation(command.introduceImageUrls(), club);
        updateProfileImageInformation(command.profileImageUrls(), club);
        clubService.update(club.getId(), command.toEntity());
        return club.getId();
    }

    private void updateIntroduceImageInformation(List<String> introduceImageUrls, Club club) {
        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
                IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + club.getId());
        if (!introduceImageUrls.isEmpty()) {
            List<FileInformation> deleteInformation = fileInformation.stream()
                    .filter(information -> !introduceImageUrls
                            .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                                    .getFileType() + information.getFileDomainCategory().getFileDomain()
                                    + information.getStoredName()))
                    .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(fileInformation);
        }
    }

    private void updateProfileImageInformation(List<String> profileImageUrls, Club club) {
        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
                IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId());
        if (!profileImageUrls.isEmpty()) {
            List<FileInformation> deleteInformation = fileInformation.stream()
                    .filter(information -> !profileImageUrls
                            .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                                    .getFileType() + information.getFileDomainCategory().getFileDomain()
                                    + information.getStoredName()))
                    .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(fileInformation);
        }
    }

}
