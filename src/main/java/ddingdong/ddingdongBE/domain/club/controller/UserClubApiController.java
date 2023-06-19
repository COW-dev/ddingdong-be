package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.controller.dto.response.ClubResponse;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class UserClubApiController {

    private final ClubService clubService;

    @GetMapping
    public List<ClubResponse> getClubs() {
        return clubService.getAllClubs();
    }

}
