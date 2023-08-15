package ddingdong.ddingdongBE.domain.fixzone.controller;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.*;
import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.*;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneService;
import ddingdong.ddingdongBE.file.service.FileService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/club/fix")
@RequiredArgsConstructor
public class ClubFixZoneController {

	private final ClubRepository clubRepository;
	private final FixZoneService fixZoneService;
	private final FileService fileService;

	@PostMapping
	public void createFix(@AuthenticationPrincipal PrincipalDetails principalDetails,
		@ModelAttribute CreateFixRequest request,
		@RequestPart(name = "images") List<MultipartFile> images) {
		Club club = clubRepository.findByUserId(principalDetails.getUser().getId())
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_CLUB.getText()));
		Long createdFixId = fixZoneService.create(club, request);

		fileService.uploadFile(createdFixId, images, IMAGE, FIX_ZONE);
	}

	@PatchMapping("/{fixId}")
	public void updateFix(@PathVariable Long fixId, @ModelAttribute UpdateFixRequest request,
		@RequestPart(name = "images") List<MultipartFile> images) {
		fixZoneService.update(fixId, request);

		fileService.deleteFile(fixId, IMAGE, FIX_ZONE);
		fileService.uploadFile(fixId, images, IMAGE, FIX_ZONE);
	}
}
