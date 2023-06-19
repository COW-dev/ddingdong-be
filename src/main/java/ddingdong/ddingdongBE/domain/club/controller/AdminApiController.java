package ddingdong.ddingdongBE.domain.club.controller;

import ddingdong.ddingdongBE.domain.club.controller.dto.request.RegisterClubRequest;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/clubs")
public class AdminApiController {

    private final ClubService clubService;

    @PostMapping
    public void register(@ModelAttribute RegisterClubRequest registerClubRequest) {
        clubService.register(registerClubRequest);
    }

}
