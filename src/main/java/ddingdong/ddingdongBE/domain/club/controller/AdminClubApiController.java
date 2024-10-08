package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.AdminClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
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

    private final ClubService clubService;

    @PostMapping
    public void register(@RequestBody RegisterClubRequest registerClubRequest) {
        clubService.create(registerClubRequest);
    }

    @GetMapping
    public List<AdminClubResponse> getClubs() {
        return clubService.findAllForAdmin();
    }

    @DeleteMapping("/{clubId}")
    public void deleteClub(@PathVariable Long clubId) {
        clubService.delete(clubId);
    }
}
