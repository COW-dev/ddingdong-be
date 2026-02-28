package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.user.entity.User;

public interface FacadeClubFeedCommentService {

    void forceDelete(User user, Long feedId, Long commentId);
}
