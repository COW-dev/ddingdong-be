package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.ClubFeedPageResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedPerClubPageResponse;
import ddingdong.ddingdongBE.domain.feed.service.FacadeFeedService;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedPageQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedQuery;
import ddingdong.ddingdongBE.domain.feed.service.dto.query.NewestFeedPerClubPageQuery;
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
  public NewestFeedPerClubPageResponse getAllFeedPage(
      int size,
      Long currentCursorId
  ) {
    NewestFeedPerClubPageQuery newestFeedPerClubPageQuery = facadeFeedService.getAllFeedPage(size, currentCursorId);
    return NewestFeedPerClubPageResponse.from(newestFeedPerClubPageQuery);
  }

  @Override
  public FeedResponse getByFeedId(Long feedId) {
    FeedQuery feedquery = facadeFeedService.getById(feedId);
    return FeedResponse.from(feedquery);
  }
}
