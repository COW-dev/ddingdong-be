package ddingdong.ddingdongBE.domain.banner.api;

import ddingdong.ddingdongBE.domain.banner.controller.dto.response.UserBannerListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Banner - User", description = "Banner User API")
@RequestMapping("/server/banners")
public interface UserBannerApi {

    @Operation(summary = "Banner 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "배너 목록 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserBannerListResponse.class))))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<UserBannerListResponse> findAllBanners();

}
