package ddingdong.ddingdongBE.domain.event.controller.dto.request;

import javax.validation.constraints.NotBlank;

public record ApplyEventRequest(
    @NotBlank(message = "학번은 빈 값이 될 수 없습니다.")
    String studentNumber,

    @NotBlank(message = "전화번호는 빈 값이 될 수 없습니다.")
    String telephone
) {

}
