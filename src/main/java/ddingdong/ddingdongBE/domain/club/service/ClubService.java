package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.BEFORE_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.END_RECRUIT;
import static ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus.RECRUITING;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.common.exception.PersistenceException;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubListResponse;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.RecruitmentStatus;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @Transactional
    public Long create(RegisterClubRequest request) {
        User clubUser = authService.registerClubUser(request.getUserId(), request.getPassword(), request.getClubName());

        Club club = request.toEntity(clubUser);
        Club savedClub = clubRepository.save(club);

        return savedClub.getId();
    }

    public List<ClubListResponse> findAllWithRecruitTimeCheckPoint(LocalDateTime now) {
        return clubRepository.findAll().stream()
                .map(club -> ClubListResponse.of(club, checkRecruit(now, club).getText()))
                .toList();
    }

    public List<AdminClubResponse> findAllForAdmin() {
        return clubRepository.findAll().stream()
                .map(club -> AdminClubResponse.of(club, fileInformationService.getImageUrls(
                        IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId())))
                .toList();
    }

    @Transactional
    public void delete(Long clubId) {
        Club club = getByClubId(clubId);

        clubRepository.delete(club);
    }

    @Transactional
    public BigDecimal updateClubScore(Long clubId, BigDecimal score) {
        Club club = getByClubId(clubId);

        return club.editScore(generateNewScore(club.getScore(), score));
    }

    @Transactional
    public Long update(Long userId, Club updatedClub) {
        Club club = getByUserId(userId);
        club.updateClubInfo(updatedClub);
        return club.getId();
    }

    public Club getByUserId(final Long userId) {
        return clubRepository.findByUserId(userId)
            .orElseThrow(() -> new PersistenceException.ResourceNotFound("Club(userId=" + userId + "를 찾을 수 없습니다."));
    }

    public Club getByClubId(final Long clubId) {
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new PersistenceException.ResourceNotFound("존재하지 않는 동아리입니다."));
    }

    private Score generateNewScore(Score beforeUpdateScore, BigDecimal value) {
        BigDecimal currentValue = beforeUpdateScore.getValue();
        BigDecimal newValue = currentValue.add(value);
        return Score.from(newValue);
    }

    private RecruitmentStatus checkRecruit(LocalDateTime now, Club club) {
        if (club.getStartRecruitPeriod() == null || club.getEndRecruitPeriod() == null
                || club.getStartRecruitPeriod().isAfter(now)) {
            return BEFORE_RECRUIT;
        }

        return club.getEndRecruitPeriod().isAfter(now) ? RECRUITING : END_RECRUIT;
    }

}
