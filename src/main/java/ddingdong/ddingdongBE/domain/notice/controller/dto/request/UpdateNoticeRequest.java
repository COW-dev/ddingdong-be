package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import java.util.List;

public record UpdateNoticeRequest(
    String title,
    String content,
    List<String>imgUrls,
    List<String> fileUrls
) {

}
