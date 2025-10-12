package ddingdong.ddingdongBE.domain.banner.controller.dto.response;

import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserBannerListResponse",
        description = "유저 - 배너 목록 조회 응답"
)
public record UserBannerListResponse(
        @Schema(description = "배너 식별자", example = "1")
        Long id,
        @Schema(description = "연결 링크", example = "https://test.com")
        String link,
        UserBannerListImageUrlResponse webImageUrl,
        UserBannerListImageUrlResponse mobileImageUrl
) {

    public static UserBannerListResponse from(UserBannerListQuery query) {
        return new UserBannerListResponse(
                query.id(),
                query.link(),
                UserBannerListImageUrlResponse.from(query.webImageUrlQuery()),
                UserBannerListImageUrlResponse.from(query.mobileImageUrlQuery())
        );
    }

    @Schema(
            name = "UserBannerListImageUrlResponse",
            description = "유저 - 배너 목록 이미지 URL 조회 응답"
    )
    record UserBannerListImageUrlResponse(
            @Schema(description = "원본 url", example = "url")
            String originUrl,
            @Schema(description = "cdn url", example = "url")
            String cdnUrl
    ) {

        public static UserBannerListImageUrlResponse from(UploadedFileUrlAndNameQuery query) {
            if (query == null) {
                return null;
            }
            return new UserBannerListImageUrlResponse(query.originUrl(), query.cdnUrl());
        }

    }

}
