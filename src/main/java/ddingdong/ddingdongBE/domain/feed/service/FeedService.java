package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

  private final FeedRepository feedRepository;

  public List<Feed> getAllByClubId(Long clubId) {
    return feedRepository.findAllByClubIdOrderById(clubId);
  }

  public List<Feed> getNewestAll() {
    return feedRepository.findNewestAll();
  }

  public Feed getById(Long feedId) {
    return feedRepository.findById(feedId)
        .orElseThrow(() -> new ResourceNotFound("Feed(id: " + feedId + ")를 찾을 수 없습니다."));
  }
}
