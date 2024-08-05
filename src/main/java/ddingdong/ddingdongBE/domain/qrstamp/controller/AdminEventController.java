package ddingdong.ddingdongBE.domain.qrstamp.controller;

import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.AppliedUsersResponse;
import ddingdong.ddingdongBE.domain.qrstamp.controller.dto.response.DetailAppliedUserResponse;
import ddingdong.ddingdongBE.domain.qrstamp.service.QrStampService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final QrStampService qrStampService;

    @GetMapping("/applied-users")
    public List<AppliedUsersResponse> getAppliedUsers() {
        return qrStampService.findAllAppliedUsers();
    }

    @GetMapping("/applied-users/{appliedUserId}")
    public DetailAppliedUserResponse getAppliedUsers(@PathVariable Long appliedUserId) {
        return qrStampService.findAppliedUser(appliedUserId);
    }

}
