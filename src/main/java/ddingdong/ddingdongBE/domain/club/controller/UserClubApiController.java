package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubListResponse;
import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.club.service.FacadeClubService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/clubs")
@RequiredArgsConstructor
public class UserClubApiController {

    private final ClubService clubService;
    private final FacadeClubService facadeClubService;

    @GetMapping
    public List<ClubListResponse> getClubs() {
        return clubService.findAllWithRecruitTimeCheckPoint(LocalDateTime.now());
    }

    @GetMapping("/{clubId}")
    public ClubResponse getDetailClub(@PathVariable Long clubId) {
        return facadeClubService.getClub(clubId);
    }

}
