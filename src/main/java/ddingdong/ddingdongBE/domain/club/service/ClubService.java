package ddingdong.ddingdongBE.domain.club.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_INTRODUCE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.CLUB_PROFILE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import ddingdong.ddingdongBE.auth.service.AuthService;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.UpdateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.DetailClubResponse;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fileinformation.entity.FileInformation;
import ddingdong.ddingdongBE.domain.fileinformation.repository.FileInformationRepository;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import ddingdong.ddingdongBE.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {

	private final ClubRepository clubRepository;
	private final AuthService authService;
	private final FileInformationService fileInformationService;
	private final FileStore fileStore;
	private final FileInformationRepository fileInformationRepository;

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
				.toList();
	}

	@Transactional(readOnly = true)
	public List<AdminClubResponse> getAllForAdmin() {
		return clubRepository.findAll().stream()
				.map(club -> AdminClubResponse.of(club, fileInformationService.getImageUrls(
						IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId())))
				.toList();
	}

	@Transactional(readOnly = true)
	public DetailClubResponse getClub(Long clubId) {
		Club club = findClubByClubId(clubId);

		List<String> profileImageUrl = fileInformationService.getImageUrls(
				IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + clubId);

		List<String> introduceImageUrls = fileInformationService.getImageUrls(
				IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + clubId);

		return DetailClubResponse.of(club, profileImageUrl, introduceImageUrls);
	}

	@Transactional(readOnly = true)
	public DetailClubResponse getMyClub(Long userId) {
		Club club = findClubByUserId(userId);

		List<String> profileImageUrl = fileInformationService.getImageUrls(
				IMAGE.getFileType() + CLUB_PROFILE.getFileDomain() + club.getId());

		List<String> introduceImageUrls = fileInformationService.getImageUrls(
				IMAGE.getFileType() + CLUB_INTRODUCE.getFileDomain() + club.getId());

		return DetailClubResponse.of(club, profileImageUrl, introduceImageUrls);
	}

	public void delete(Long clubId) {
		Club club = findClubByClubId(clubId);

		clubRepository.delete(club);
	}

	public int editClubScore(Long clubId, int score) {
		Club club = findClubByClubId(clubId);

		return club.editScore(score);
	}

	public Long update(Long userId, UpdateClubRequest request) {
		Club club = findClubByUserId(userId);
		updateIntroduceImageInformation(request, club);
		updateProfileImageInformation(request, club);

		club.updateClubInfo(request);
		return club.getId();
	}

	public Club findClubByUserId(final Long userId) {
		return clubRepository.findByUserId(userId)
				.orElseThrow(() -> new NoSuchElementException(NO_SUCH_CLUB.getText()));
	}

	public Club findClubByClubId(final Long clubId) {
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
}