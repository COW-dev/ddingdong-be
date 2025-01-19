package ddingdong.ddingdongBE.domain.banner.service.dto.query;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;

public record UserBannerListQuery(
        Long id,
        String link,
        UploadedFileUrlQuery webImageUrlQuery,
        UploadedFileUrlQuery mobileImageUrlQuery
) {

    public static UserBannerListQuery of(
            Banner banner,
            UploadedFileUrlQuery webImageUrlQuery,
            UploadedFileUrlQuery mobileImageUrlQuery
    ) {
        return new UserBannerListQuery(banner.getId(), banner.getLink(), webImageUrlQuery, mobileImageUrlQuery);
    }


}
