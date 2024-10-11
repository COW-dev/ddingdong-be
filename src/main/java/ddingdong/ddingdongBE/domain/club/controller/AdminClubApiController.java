package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.api.AdminClubAip;
import ddingdong.ddingdongBE.domain.club.controller.dto.request.CreateClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubListResponse;
import ddingdong.ddingdongBE.domain.club.service.FacadeAdminClubService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminClubApiController implements AdminClubAip {

    private final FacadeAdminClubService facadeAdminClubService;

    @Override
    public void createClub(CreateClubRequest request) {
        facadeAdminClubService.create(request.toCommand());
    }
    @Override
    public List<AdminClubListResponse> getClubs() {
        return facadeAdminClubService.findAll().stream()
                .map(AdminClubListResponse::from)
                .toList();
    }

    @Override
    public void deleteClub(Long clubId) {
        facadeAdminClubService.deleteClub(clubId);
    }
}
