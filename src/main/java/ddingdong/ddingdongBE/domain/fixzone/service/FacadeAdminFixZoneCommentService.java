package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommentCommand;

public interface FacadeAdminFixZoneCommentService {

    Long create(CreateFixZoneCommentCommand command);

    Long update(UpdateFixZoneCommentCommand command);

    void delete(Long fixZoneId);

}
