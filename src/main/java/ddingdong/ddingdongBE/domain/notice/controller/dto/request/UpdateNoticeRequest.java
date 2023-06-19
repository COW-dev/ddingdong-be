package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNoticeRequest {

    private String title;

    private String content;

}
