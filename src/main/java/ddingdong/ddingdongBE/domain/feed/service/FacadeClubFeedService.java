package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;

public interface FacadeClubFeedService {

    void create(CreateFeedCommand command);

    void update(UpdateFeedCommand command);

}
