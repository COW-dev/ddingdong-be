package ddingdong.ddingdongBE.common.cache.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Cache - Admin", description = "Cache Admin API")
@RequestMapping("/server/admin/caches")
public interface AdminCacheApi {

    @Operation(summary = "동아리 캐시 무효화 API")
    @ApiResponse(responseCode = "204", description = "동아리 캐시 무효화 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/clubs/evict")
    void evictClubsCache();

    @Operation(summary = "신청폼 캐시 무효화 API")
    @ApiResponse(responseCode = "204", description = "신청폼 캐시 무효화 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/forms/evict")
    void evictFormsCache();

}
