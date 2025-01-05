package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;

public interface FacadeClubFeedService {

    void create(CreateFeedCommand command);
}
