package ddingdong.ddingdongBE.domain.banner.service.dto.query;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;

public record UserBannerListQuery(
        Long id,
        String link,
        UploadedFileUrlAndNameQuery webImageUrlQuery,
        UploadedFileUrlAndNameQuery mobileImageUrlQuery
) {

    public static UserBannerListQuery of(
            Banner banner,
            UploadedFileUrlAndNameQuery webImageUrlQuery,
            UploadedFileUrlAndNameQuery mobileImageUrlQuery
    ) {
        return new UserBannerListQuery(banner.getId(), banner.getLink(), webImageUrlQuery, mobileImageUrlQuery);
    }


}
