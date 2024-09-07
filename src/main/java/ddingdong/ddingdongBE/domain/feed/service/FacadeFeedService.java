package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.feed.controller.dto.response.FeedListResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.NewestFeedListResponse;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeFeedService {

  private final FeedService feedService;

  public List<FeedListInfo> getAllByClubId(Long clubId) {
    List<Feed> feeds = feedService.getAllByClubId(clubId);
    return feeds.stream()
        .map(FeedListInfo::from)
        .toList();
  }

  public List<NewestFeedListInfo> getNewestAll() {
    List<Feed> feeds = feedService.getNewestAll();
    return feeds.stream()
        .map(NewestFeedListInfo::from)
        .toList();
  }
}
