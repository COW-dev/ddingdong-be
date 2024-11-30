package ddingdong.ddingdongBE.domain.banner.repository.dto;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;

public record BannerWithFileMetaData(
        Banner banner,
        FileMetaData fileMetaData
) {

}
