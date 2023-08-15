package ddingdong.ddingdongBE.domain.fixzone.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.FIX_ZONE;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFiXCompletionRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminDetailFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.ClubDetailFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.ClubFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FixZoneService {

	private final FixRepository fixRepository;
	private final FileInformationService fileInformationService;

	public Long create(Club club, CreateFixRequest request) {
		Fix createdFix = request.toEntity(club);
		Fix savedFix = fixRepository.save(createdFix);
		return savedFix.getId();
	}

	public List<ClubFixResponse> getAllForClub() {
			return fixRepository.findAll().stream()
				.map(ClubFixResponse::from)
				.toList();
	}

	public ClubDetailFixResponse getForClub(Long fixId) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

		List<String> imageUrls = fileInformationService.getImageUrls(
			IMAGE.getFileType() + FIX_ZONE.getFileDomain() + fix.getId());

		return ClubDetailFixResponse.builder()
			.id(fix.getId())
			.title(fix.getTitle())
			.createdAt(fix.getCreatedAt())
			.content(fix.getContent())
			.imageUrls(imageUrls).build();
	}

	public List<AdminFixResponse> getAllForAdmin() {
		return fixRepository.findAll().stream()
			.map(AdminFixResponse::from)
			.toList();
	}

	public AdminDetailFixResponse getForAdmin(Long fixId) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

		List<String> imageUrls = fileInformationService.getImageUrls(
			IMAGE.getFileType() + FIX_ZONE.getFileDomain() + fix.getId());

		return AdminDetailFixResponse.builder()
			.id(fix.getId())
			.title(fix.getTitle())
			.createdAt(fix.getCreatedAt())
			.club(fix.getClub().getName())
			.location(fix.getClub().getLocation().getValue())
			.content(fix.getContent())
			.isCompleted(fix.isCompleted())
			.imageUrls(imageUrls).build();
	}

	public void update(Long fixId, UpdateFixRequest request) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

		fix.update(request);
	}

	public void updateIsCompleted(Long fixId, UpdateFiXCompletionRequest request) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

		fix.updateIsCompleted(request.isCompleted());
	}

	public void delete(Long fixId) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));
		fixRepository.delete(fix);
	}
}
