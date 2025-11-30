package ddingdong.ddingdongBE.domain.fixzone.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.api.AdminFixZoneApi;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixZoneListResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneCommentService;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneService;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFixZoneController implements AdminFixZoneApi {

    private final FacadeAdminFixZoneService facadeAdminFixZoneService;
    private final FacadeAdminFixZoneCommentService facadeAdminFixZoneCommentService;

    @Override
    public List<AdminFixZoneListResponse> getFixZones() {
        return facadeAdminFixZoneService.getAll().stream()
                .map(AdminFixZoneListResponse::from)
                .toList();
    }

    @Override
    public AdminFixZoneResponse getFixZoneDetail(Long fixZoneId) {
        AdminFixZoneQuery query = facadeAdminFixZoneService.getFixZone(fixZoneId);
        return AdminFixZoneResponse.from(query);
    }

    @Override
    public void updateFixZoneToComplete(Long fixZoneId) {
        facadeAdminFixZoneService.updateToComplete(fixZoneId);
    }

    @Override
    public void createFixZoneComment(
            PrincipalDetails principalDetails,
            CreateFixZoneCommentRequest request,
            Long fixZoneId
    ) {
        User admin = principalDetails.getUser();
        facadeAdminFixZoneCommentService.create(request.toCommand(admin, fixZoneId));
    }

    @Override
    public void updateFixZoneComment(UpdateFixZoneCommentRequest request, Long fixZoneId, Long commentId) {
        facadeAdminFixZoneCommentService.update(request.toCommand(commentId));
    }

    @Override
    public void deleteFixZoneComment(Long fixZoneId, Long commentId) {
        facadeAdminFixZoneCommentService.delete(commentId);
    }

}
