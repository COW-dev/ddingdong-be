package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;

public interface FixZoneCommentService {

    Long save(FixZoneComment fixZoneComment);

    FixZoneComment getById(Long fixZoneCommentId);

    void delete(Long fixZoneCommentId);

}
