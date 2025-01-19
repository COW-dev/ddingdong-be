package ddingdong.ddingdongBE.domain.banner.repository.dto;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;

public record BannerWithFileMetaDataDto(
        Banner banner,
        List<FileMetaData> fileMetaDataList
) {

}
