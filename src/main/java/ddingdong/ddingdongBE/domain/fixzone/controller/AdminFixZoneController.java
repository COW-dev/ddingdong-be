package ddingdong.ddingdongBE.domain.fixzone.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.fixzone.controller.api.AdminFixZoneApi;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneCommentRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneCommentService;
import ddingdong.ddingdongBE.domain.fixzone.service.FixZoneService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFixZoneController implements AdminFixZoneApi {

	private final FixZoneService fixZoneService;
    private final FixZoneCommentService fixZoneCommentService;
    private final ClubService clubService;

    @Override
    public List<GetFixZoneResponse> getFixZones() {
        return fixZoneService.getAll();
    }

    @Override
    public void updateFixZoneToComplete(Long fixZoneId) {
        fixZoneService.updateToComplete(fixZoneId);
    }

    @Override
    public void createFixZoneComment(
        PrincipalDetails principalDetails,
        CreateFixZoneCommentRequest request,
        Long fixZoneId
    ) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        Club club = clubService.getByUserId(principalDetails.getUser().getId());

        fixZoneCommentService.create(fixZone, club, request);
    }

    @Override
    public void updateFixZoneComment(
        PrincipalDetails principalDetails,
        CreateFixZoneCommentRequest request,
        Long fixZoneId,
        Long commentId
    ) {
        Club club = clubService.getByUserId(principalDetails.getUser().getId());

        fixZoneCommentService.update(club.getId(), commentId, request);
    }

    @Override
    public void deleteFixZoneComment(
        PrincipalDetails principalDetails,
        Long fixZoneId,
        Long commentId
    ) {
        fixZoneCommentService.delete(commentId);
    }

}
