package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_CLUB;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.ClubMemberDto;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.repository.FileInformationRepository;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.FileStore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final AuthService authService;
    private final FileInformationService fileInformationService;
    private final FileStore fileStore;
    private final FileInformationRepository fileInformationRepository;

    @Transactional
    public Long create(RegisterClubRequest request) {
        User clubUser = authService.registerClubUser(request.getUserId(), request.getPassword(), request.getClubName());

        Club club = request.toEntity(clubUser);
        Club savedClub = clubRepository.save(club);

        return savedClub.getId();
    }

    public List<ClubResponse> findAllWithRecruitTimeCheckPoint(LocalDateTime now) {
        return clubRepository.findAll().stream()
                .map(club -> ClubResponse.of(club, checkRecruit(now, club).getText()))
                .toList();
    }

    public List<AdminClubResponse> findAllForAdmin() {
        return clubRepository.findAll().stream()
                .map(club -> AdminClubResponse.of(club, fileInformationService.getImageUrls(
                        IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId())))
                .toList();
    }

    public DetailClubResponse findByClubId(Long clubId) {
        Club club = getByClubId(clubId);

        List<String> profileImageUrl = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + clubId);

        List<String> introduceImageUrls = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + clubId);

        List<ClubMemberDto> clubMemberDtos = club.getClubMembers().stream()
                .map(ClubMemberDto::from)
                .toList();

        return DetailClubResponse.of(club, profileImageUrl, introduceImageUrls, clubMemberDtos);
    }

    public DetailClubResponse getMyClub(Long userId) {
        Club club = getByUserId(userId);

        List<String> profileImageUrl = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId());

        List<String> introduceImageUrls = fileInformationService.getImageUrls(
                IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + club.getId());

        List<ClubMemberDto> clubMemberDtos = club.getClubMembers().stream()
                .map(ClubMemberDto::from)
                .toList();

        return DetailClubResponse.of(club, profileImageUrl, introduceImageUrls, clubMemberDtos);
    }

    @Transactional
    public void delete(Long clubId) {
        Club club = getByClubId(clubId);

        clubRepository.delete(club);
    }

    @Transactional
    public float updateClubScore(Long clubId, float score) {
        Club club = getByClubId(clubId);

        return club.editScore(generateNewScore(club.getScore(), score));
    }

    @Transactional
    public Long update(Long userId, UpdateClubRequest request) {
        Club club = getByUserId(userId);
        updateIntroduceImageInformation(request, club);
        updateProfileImageInformation(request, club);

        club.updateClubInfo(request);
        return club.getId();
    }

    public Club getByUserId(final Long userId) {
        return clubRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB.getText()));
    }

    public Club getByClubId(final Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB.getText()));
    }

    private void updateIntroduceImageInformation(UpdateClubRequest request, Club club) {
        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
                IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + club.getId());
        if (!request.getIntroduceImageUrls().isEmpty()) {
            List<FileInformation> deleteInformation = fileInformation.stream()
                    .filter(information -> !request.getIntroduceImageUrls()
                            .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                                    .getFileType() + information.getFileDomainCategory().getFileDomain()
                                    + information.getStoredName()))
                    .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(fileInformation);
        }
    }

    private void updateProfileImageInformation(UpdateClubRequest request, Club club) {
        List<FileInformation> fileInformation = fileInformationService.getFileInformation(
                IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId());
        if (!request.getProfileImageUrls().isEmpty()) {
            List<FileInformation> deleteInformation = fileInformation.stream()
                    .filter(information -> !request.getProfileImageUrls()
                            .contains(fileStore.getImageUrlPrefix() + information.getFileTypeCategory()
                                    .getFileType() + information.getFileDomainCategory().getFileDomain()
                                    + information.getStoredName()))
                    .toList();

            fileInformationRepository.deleteAll(deleteInformation);
        } else {
            fileInformationRepository.deleteAll(fileInformation);
        }
    }

    private Score generateNewScore(Score beforeUpdateScore, float value) {
        return Score.from(beforeUpdateScore.getValue() + value);
    }

    private RecruitmentStatus checkRecruit(LocalDateTime now, Club club) {
        if (club.getStartRecruitPeriod() == null || club.getEndRecruitPeriod() == null
                || club.getStartRecruitPeriod().isAfter(now)) {
            return BEFORE_RECRUIT;
        }

        return club.getEndRecruitPeriod().isAfter(now) ? RECRUITING : END_RECRUIT;
    }

}
