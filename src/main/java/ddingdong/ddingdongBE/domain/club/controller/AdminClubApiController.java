package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.CreateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubListResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeAdminClubService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/admin/clubs")
public class AdminClubApiController {

    private final FacadeAdminClubService facadeAdminClubService;

    @PostMapping
    public void register(@RequestBody CreateClubRequest request) {
        facadeAdminClubService.create(request.toCommand());
    }

    @GetMapping
    public List<AdminClubListResponse> getClubs() {
        return facadeAdminClubService.findAll().stream()
                .map(AdminClubListResponse::from)
                .toList();
    }

    @DeleteMapping("/{clubId}")
    public void deleteClub(@PathVariable Long clubId) {
        facadeAdminClubService.deleteClub(clubId);
    }
}
