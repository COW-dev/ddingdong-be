package ddingdong.ddingdongBE.domain.notice.service;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;

public interface FacadeAdminNoticeService {

    void create(CreateNoticeCommand command);

    void update(UpdateNoticeCommand command);

    void delete(Long noticeId);

}
