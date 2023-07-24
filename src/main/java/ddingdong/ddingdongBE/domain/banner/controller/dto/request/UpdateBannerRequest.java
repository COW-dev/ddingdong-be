package ddingdong.ddingdongBE.domain.banner.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBannerRequest {

    private String title;

    private String subTitle;

    private String colorCode;

}
