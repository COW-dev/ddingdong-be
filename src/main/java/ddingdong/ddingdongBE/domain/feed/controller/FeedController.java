package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.ClubFeedPageResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedPageResponse;
import ddingdong.ddingdongBE.domain.feed.service.FacadeFeedService;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedPageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedController implements FeedApi {

  private final FacadeFeedService facadeFeedService;

  @Override
  public ClubFeedPageResponse getFeedPageByClub(
          Long clubId,
          int size,
          Long currentCursorId
  ) {
    ClubFeedPageQuery clubFeedPageQuery = facadeFeedService.getFeedPageByClub(clubId, size, currentCursorId);
    return ClubFeedPageResponse.from(clubFeedPageQuery);
  }

  @Override
  public FeedPageResponse getAllFeedPage(
          int size,
          Long currentCursorId
  ) {
    FeedPageQuery feedPageQuery = facadeFeedService.getAllFeedPage(size, currentCursorId);
    return FeedPageResponse.from(feedPageQuery);
  }

  @Override
  public FeedResponse getByFeedId(Long feedId) {
    FeedQuery feedquery = facadeFeedService.getById(feedId);
    return FeedResponse.from(feedquery);
  }
}
