package ddingdong.ddingdongBE.domain.banner.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.CreateBannerRequest;
import ddingdong.ddingdongBE.domain.banner.controller.dto.response.AdminBannerListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Banner - Admin", description = "Banner Admin API")
@RequestMapping("/server/admin/banners")
public interface AdminBannerApi {

    @Operation(summary = "Banner 생성 API")
    @ApiResponse(responseCode = "201", description = "배너 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping
    void createBanner(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateBannerRequest request
    );

    @Operation(summary = "Banner 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "배너 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminBannerListResponse.class))))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping()
    List<AdminBannerListResponse> findAllBanners();

    @Operation(summary = "Banner 삭제 API")
    @ApiResponse(responseCode = "204", description = "배너 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{bannerId}")
    void deleteBanner(@PathVariable("bannerId") Long bannerId);

}
