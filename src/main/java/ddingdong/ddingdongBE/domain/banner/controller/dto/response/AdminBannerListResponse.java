package ddingdong.ddingdongBE.domain.banner.controller.dto.response;

import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AdminBannerListResponse",
        description = "어드민 - 배너 목록 조회 응답"
)
public record AdminBannerListResponse(
        @Schema(description = "배너 식별자", example = "1")
        Long id,
        @Schema(description = "연결 링크", example = "https://test.com")
        String link,
        AdminBannerListImageUrlResponse webImageUrl,
        AdminBannerListImageUrlResponse mobileImageUrl
) {

    public static AdminBannerListResponse from(AdminBannerListQuery query) {
        return new AdminBannerListResponse(
                query.id(),
                query.link(),
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
            String cdnUrl,
            @Schema(description = "파일 이름", example = "filename.jpg")
            String filename
    ) {

        public static AdminBannerListImageUrlResponse from(UploadedFileUrlAndNameQuery query) {
            if (query == null) {
                return null;
            }
            return new AdminBannerListImageUrlResponse(query.originUrl(), query.cdnUrl(), query.fileName());
        }

    }

}
