package ddingdong.ddingdongBE.domain.banner.service.dto.query;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;

public record AdminBannerListQuery(
        Long id,
        UploadedImageUrlQuery webImageUrlQuery,
        UploadedImageUrlQuery mobileImageUrlQuery
) {

    public static AdminBannerListQuery of(
        Banner banner, UploadedImageUrlQuery webImageUrlQuery,
        UploadedImageUrlQuery mobileImageUrlQuery
    ) {
        return new AdminBannerListQuery(banner.getId(), webImageUrlQuery, mobileImageUrlQuery);
    }

}
