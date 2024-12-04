package ddingdong.ddingdongBE.domain.banner.service.dto.query;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;

public record AdminBannerListQuery(
        Long id,
        String link,
        UploadedFileUrlQuery webImageUrlQuery,
        UploadedFileUrlQuery mobileImageUrlQuery
) {

    public static AdminBannerListQuery of(
            Banner banner,
            UploadedFileUrlQuery webImageUrlQuery,
            UploadedFileUrlQuery mobileImageUrlQuery
    ) {
        return new AdminBannerListQuery(banner.getId(), banner.getLink(), webImageUrlQuery, mobileImageUrlQuery);
    }

}
