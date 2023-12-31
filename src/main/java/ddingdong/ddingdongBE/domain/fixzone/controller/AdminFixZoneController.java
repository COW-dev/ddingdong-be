package ddingdong.ddingdongBE.domain.fixzone.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFiXCompletionRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminDetailFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixResponse;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/server/admin/fix")
@RequiredArgsConstructor
public class AdminFixZoneController {

	private final FixZoneService fixZoneService;

	@GetMapping
	public List<AdminFixResponse> getAllFixForAdmin() {
		return fixZoneService.getAllForAdmin();
	}

	@GetMapping("/{fixId}")
	public AdminDetailFixResponse getFixForAdmin(@PathVariable Long fixId) {
		return fixZoneService.getForAdmin(fixId);
	}

	@PatchMapping("/{fixId}")
	public void updateFix(@PathVariable Long fixId, @RequestBody UpdateFiXCompletionRequest request) {
		fixZoneService.updateIsCompleted(fixId, request);
	}
}
