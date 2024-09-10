package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedListResponse;
import ddingdong.ddingdongBE.domain.feed.service.FacadeFeedService;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedListQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController implements FeedApi {

  private final FacadeFeedService facadeFeedService;

  @Override
  public List<FeedListResponse> getAllFeedByClubId(Long clubId) {
    List<FeedListQuery> feedListQueries = facadeFeedService.getAllByClubId(clubId);
    return feedListQueries.stream()
        .map(FeedListResponse::from)
        .toList();
  }

  @Override
  public List<NewestFeedListResponse> getNewestAllFeed() {
    List<FeedListQuery> newestFeedListQueries = facadeFeedService.getNewestAll();
    return newestFeedListQueries.stream()
        .map(NewestFeedListResponse::from)
        .toList();
  }

  @Override
  public FeedResponse getByFeedId(Long feedId) {
    FeedQuery feedquery = facadeFeedService.getById(feedId);
    return FeedResponse.from(feedquery);
  }
}
