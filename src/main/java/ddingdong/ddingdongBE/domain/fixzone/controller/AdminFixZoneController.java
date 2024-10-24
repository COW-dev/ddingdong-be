package ddingdong.ddingdongBE.domain.fixzone.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.api.AdminFixZoneApi;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixZoneListResponse;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneCommentServiceImpl;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneServiceImpl;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFixZoneController implements AdminFixZoneApi {

    private final FacadeAdminFixZoneServiceImpl facadeAdminFixZoneServiceImpl;
    private final FacadeAdminFixZoneCommentServiceImpl facadeAdminFixZoneCommentService;

    @Override
    public List<AdminFixZoneListResponse> getFixZones() {
        return facadeAdminFixZoneServiceImpl.getAll().stream()
                .map(AdminFixZoneListResponse::from)
                .toList();
    }

    @Override
    public void updateFixZoneToComplete(Long fixZoneId) {
        facadeAdminFixZoneServiceImpl.updateToComplete(fixZoneId);
    }

    @Override
    public void createFixZoneComment(
            PrincipalDetails principalDetails,
            CreateFixZoneCommentRequest request,
            Long fixZoneId
    ) {
        User admin = principalDetails.getUser();
        facadeAdminFixZoneCommentService.create(request.toCommand(admin.getId(), fixZoneId));
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
