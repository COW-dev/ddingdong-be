package ddingdong.ddingdongBE.domain.club.service;


import static ddingdong.ddingdongBE.domain.imageinformation.entity.ImageCategory.CLUB;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.imageinformation.service.ImageInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {

    private static final String NO_SUCH_CLUB_EXCEPTION = "해당 동아리가 존재하지 않습니다.";

    private final ClubRepository clubRepository;
    private final AuthService authService;
    private final ImageInformationService imageInformationService;

    public Long register(RegisterClubRequest request) {
        User clubUser = authService.registerClubUser(request.getUserId(), request.getPassword(), request.getClubName());

        Club club = request.toEntity(clubUser);
        Club savedClub = clubRepository.save(club);

        return savedClub.getId();
    }

    @Transactional(readOnly = true)
    public List<ClubResponse> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(ClubResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminClubResponse> getAllForAdmin() {
        return clubRepository.findAll().stream()
                .map(AdminClubResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetailClubResponse getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB_EXCEPTION));

        List<String> imageUrls = imageInformationService.getImageUrls(CLUB.getFilePath() + clubId);

        return DetailClubResponse.of(club, imageUrls);
    }

    @Transactional(readOnly = true)
    public DetailClubResponse getMyClub(Long userId) {
        Club club = clubRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB_EXCEPTION));

        List<String> imageUrls = imageInformationService.getImageUrls(CLUB.getFilePath() + club.getId());

        return DetailClubResponse.of(club, imageUrls);
    }

    public void delete(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB_EXCEPTION));

        clubRepository.delete(club);
    }

    public void editClubScore(Long clubId, int score) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB_EXCEPTION));

        club.editScore(score);
    }

    public Long update(Long userId, UpdateClubRequest request) {
        Club club = clubRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB_EXCEPTION));

        club.updateClubInfo(request);
        return club.getId();
    }

}
