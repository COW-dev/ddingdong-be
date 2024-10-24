package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import lombok.Builder;

@Builder
public record GetNoticeListCommand(
    int page,
    int limit
) {

}
