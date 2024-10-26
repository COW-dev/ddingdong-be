package ddingdong.ddingdongBE.domain.banner.service.dto.query;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedImageUrlQuery;

public record UserBannerListQuery(
        Long id,
        UploadedImageUrlQuery webImageUrlQuery,
        UploadedImageUrlQuery mobileImageUrlQuery
) {

    public static UserBannerListQuery of(
            Banner banner,
        UploadedImageUrlQuery webImageUrlQuery,
        UploadedImageUrlQuery mobileImageUrlQuery
    ) {
        return new UserBannerListQuery(banner.getId(), webImageUrlQuery, mobileImageUrlQuery);
    }


}
