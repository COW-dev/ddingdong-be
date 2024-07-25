package ddingdong.ddingdongBE.domain.fixzone.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetDetailFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.GetFixZoneResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Fix Zone - Club", description = "Fix Zone Club API")
@RequestMapping(value = "/server/club/fix-zones", produces = APPLICATION_JSON_VALUE)
public interface ClubFixZoneApi {

    @Operation(summary = "클럽별 등록한 Fix Zone 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<GetFixZoneResponse> getMyFixZones(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(summary = "Fix Zone 상세 조회")
    @GetMapping("/{fix-zone-id}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    GetDetailFixZoneResponse getFixZoneDetail(@PathVariable("fix-zone-id") Long fixZoneId);

    @Operation(summary = "Fix Zone 등록 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void createFixZone(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart CreateFixZoneRequest request,
        @RequestPart(name = "images", required = false) List<MultipartFile> images
    );

    @Operation(summary = "Fix Zone 수정 API")
    @PatchMapping(value = "/{fix-zone-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateFixZone(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable("fix-zone-id") Long fixZoneId,
        @RequestPart UpdateFixZoneRequest request,
        @RequestPart(name = "images", required = false) List<MultipartFile> images
    );

    @Operation(summary = "Fix Zone 삭제 API")
    @DeleteMapping("/{fix-zone-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteFixZone(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable("fix-zone-id") Long fixZoneId
    );

}
