package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNoticeRequest {

    private String title;

    private String content;

    private List<String> imgUrls;

    private List<String> fileUrls;

}
