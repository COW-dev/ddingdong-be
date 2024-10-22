//package ddingdong.ddingdongBE.domain.fixzone.controller;
//
//import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileDomainCategory.FIX_ZONE;
//import static ddingdong.ddingdongBE.domain.fileinformation.entity.FileTypeCategory.IMAGE;
//
//import ddingdong.ddingdongBE.auth.PrincipalDetails;
//import ddingdong.ddingdongBE.domain.club.entity.Club;
//import ddingdong.ddingdongBE.domain.club.service.ClubService;
//import ddingdong.ddingdongBE.domain.fixzone.controller.api.ClubFixZoneApi;
//import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
//import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
//import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetDetailFixZoneResponse;
//import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneResponse;
//import ddingdong.ddingdongBE.domain.fixzone.service.FacadeAdminFixZoneService;
//import ddingdong.ddingdongBE.domain.fixzone.service.FacadeCentralFixZoneService;
//import ddingdong.ddingdongBE.file.service.FileService;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequiredArgsConstructor
//public class ClubFixZoneController implements ClubFixZoneApi {
//    private final ClubService clubService;
//    private final FacadeCentralFixZoneService facadeCentralFixZoneService;
//    private final FileService fileService;
//
//    @Override
//    public List<GetFixZoneResponse> getMyFixZones(PrincipalDetails principalDetails) {
//        Club club = clubService.getByUserId(principalDetails.getUser().getId());
//
//        return facadeCentralFixZoneService.getMyFixZones(club.getId());
//    }
//
//    @Override
//    public GetDetailFixZoneResponse getFixZoneDetail(Long id) {
//        return facadeCentralFixZoneService.getFixZoneDetail(id);
//    }
//
//    @Override
//    public void createFixZone(
//        PrincipalDetails principalDetails,
//        CreateFixZoneRequest request,
//        List<MultipartFile> images
//    ) {
//        Club club = clubService.getByUserId(principalDetails.getUser().getId());
//        Long createdFixZoneId = facadeCentralFixZoneService.create(club, request);
//
//        fileService.uploadFile(createdFixZoneId, images, IMAGE, FIX_ZONE);
//    }
//
//    @Override
//    public void updateFixZone(
//        PrincipalDetails principalDetails,
//        Long id,
//        UpdateFixZoneRequest request,
//        List<MultipartFile> images
//    ) {
//        clubService.getByUserId(principalDetails.getUser().getId());
//
//        facadeCentralFixZoneService.update(id, request);
//
//        fileService.deleteFile(id, IMAGE, FIX_ZONE);
//        fileService.uploadFile(id, images, IMAGE, FIX_ZONE);
//    }
//
//    @Override
//    public void deleteFixZone(
//        PrincipalDetails principalDetails,
//        Long id
//    ) {
//        clubService.getByUserId(principalDetails.getUser().getId());
//
//        facadeCentralFixZoneService.delete(id);
//
//        fileService.deleteFile(id, IMAGE, FIX_ZONE);
//    }
//
//}
