package ddingdong.ddingdongBE.domain.fixzone.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.api.ClubFixZoneApi;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.CentralFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.CentralMyFixZoneListResponse;
import ddingdong.ddingdongBE.domain.fixzone.service.FacadeCentralFixZoneServiceImpl;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubFixZoneController implements ClubFixZoneApi {

    private final FacadeCentralFixZoneServiceImpl facadeCentralFixZoneServiceImpl;

    @Override
    public List<CentralMyFixZoneListResponse> getMyFixZones(PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        return facadeCentralFixZoneServiceImpl.getMyFixZones(user.getId()).stream()
                .map(CentralMyFixZoneListResponse::from)
                .toList();
    }

    @Override
    public CentralFixZoneResponse getFixZoneDetail(Long id) {
        CentralFixZoneQuery query = facadeCentralFixZoneServiceImpl.getFixZone(id);
        return CentralFixZoneResponse.from(query);
    }

    @Override
    public void createFixZone(PrincipalDetails principalDetails, CreateFixZoneRequest request) {
        User user = principalDetails.getUser();
        facadeCentralFixZoneServiceImpl.create(request.toCommand(user.getId()));
    }

    @Override
    public void updateFixZone(Long fixZoneId, UpdateFixZoneRequest request) {
        facadeCentralFixZoneServiceImpl.update(request.toCommand(fixZoneId));

    }

    @Override
    public void deleteFixZone(Long fixZoneId) {
        facadeCentralFixZoneServiceImpl.delete(fixZoneId);
    }

}
