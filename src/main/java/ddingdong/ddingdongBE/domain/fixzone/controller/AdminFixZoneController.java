package ddingdong.ddingdongBE.domain.fixzone.controller;

import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.fixzone.controller.api.AdminFixZoneApi;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.AdminFixZoneListResponse;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneCommentService;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFixZoneController implements AdminFixZoneApi {

	private final FacadeAdminFixZoneServiceImpl facadeAdminFixZoneServiceImpl;
    private final FixZoneCommentService fixZoneCommentService;
    private final ClubService clubService;

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

//    @Override
//    public void createFixZoneComment(
//            PrincipalDetails principalDetails,
//            CreateFixZoneCommentRequest request,
//            Long fixZoneId
//    ) {
//        FixZone fixZone = facadeAdminFixZoneService.getById(fixZoneId);
//        Club club = clubService.getByUserId(principalDetails.getUser().getId());
//
//        fixZoneCommentService.create(fixZone, club, request);
//    }
//
//    @Override
//    public void updateFixZoneComment(
//        PrincipalDetails principalDetails,
//        CreateFixZoneCommentRequest request,
//        Long fixZoneId,
//        Long commentId
//    ) {
//        Club club = clubService.getByUserId(principalDetails.getUser().getId());
//
//        fixZoneCommentService.update(club.getId(), commentId, request);
//    }
//
//    @Override
//    public void deleteFixZoneComment(
//        PrincipalDetails principalDetails,
//        Long fixZoneId,
//        Long commentId
//    ) {
//        fixZoneCommentService.delete(commentId);
//    }

}
