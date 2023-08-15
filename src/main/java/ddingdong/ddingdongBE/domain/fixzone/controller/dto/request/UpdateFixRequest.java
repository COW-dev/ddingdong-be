package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateFixRequest {

	private String title;

	private String content;

	private String imgUrls;
}
