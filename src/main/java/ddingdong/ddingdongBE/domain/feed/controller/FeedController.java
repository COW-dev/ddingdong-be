package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedListResponse;
import ddingdong.ddingdongBE.domain.feed.service.FacadeFeedService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController implements FeedApi {

  private final FacadeFeedService facadeFeedService;

  @Override
  public List<FeedListResponse> getAllFeedByClubId(Long clubId) {
    return facadeFeedService.getAllByClubId(clubId);
  }

  @Override
  public List<NewestFeedListResponse> getNewestAllFeed() {
    return facadeFeedService.getNewestAll();
  }
}
