package ddingdong.ddingdongBE.domain.fixzone.controller.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixZoneRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.CentralFixZoneResponse;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.response.CentralMyFixZoneListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Fix Zone - Club", description = "Fix Zone Club API")
@RequestMapping(value = "/server/central/fix-zones", produces = APPLICATION_JSON_VALUE)
public interface ClubFixZoneApi {

    @Operation(summary = "클럽별 등록한 Fix Zone 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<CentralMyFixZoneListResponse> getMyFixZones(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(summary = "Fix Zone 상세 조회")
    @GetMapping("/{fixZoneId}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    CentralFixZoneResponse getFixZoneDetail(@PathVariable("fixZoneId") Long fixZoneId);

    @Operation(summary = "Fix Zone 등록 API")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void createFixZone(@AuthenticationPrincipal PrincipalDetails principalDetails,
                       @RequestBody CreateFixZoneRequest request);

    @Operation(summary = "Fix Zone 수정 API")
    @PatchMapping(value = "/{fixZoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateFixZone(
            @PathVariable("fixZoneId") Long fixZoneId,
            @RequestBody UpdateFixZoneRequest request
    );

    @Operation(summary = "Fix Zone 삭제 API")
    @DeleteMapping("/{fixZoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteFixZone(
            @PathVariable("fixZoneId") Long fixZoneId
    );

}
