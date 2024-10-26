package ddingdong.ddingdongBE.domain.banner.controller.dto.response;

import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AdminBannerListResponse",
        description = "어드민 - 배너 목록 조회 응답"
)
public record AdminBannerListResponse(
        @Schema(description = "배너 식별자", example = "1")
        Long id,
        AdminBannerListImageUrlResponse webImageUrl,
        AdminBannerListImageUrlResponse mobileImageUrl
) {

    public static AdminBannerListResponse from(AdminBannerListQuery query) {
        return new AdminBannerListResponse(
                query.id(),
                AdminBannerListImageUrlResponse.from(query.webImageUrlQuery()),
                AdminBannerListImageUrlResponse.from(query.mobileImageUrlQuery())
        );
    }

    @Schema(
            name = "AdminBannerListImageUrlResponse",
            description = "어드민 - 배너 목록 이미지 URL 조회 응답"
    )
    record AdminBannerListImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static AdminBannerListImageUrlResponse from(UploadedImageUrlQuery query) {
            if (query == null) {
                return null;
            }
            return new AdminBannerListImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }

}
