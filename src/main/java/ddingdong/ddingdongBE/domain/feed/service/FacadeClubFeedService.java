package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.MyFeedPageQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;

public interface FacadeClubFeedService {

    void create(CreateFeedCommand command);

    void update(UpdateFeedCommand command);

    void delete(Long feedId);

    MyFeedPageQuery getMyFeedPage(User user, int size, Long currentCursorId);
}
