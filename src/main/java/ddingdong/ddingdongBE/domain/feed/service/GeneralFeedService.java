package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedService implements FeedService {

    private final FeedRepository feedRepository;

    @Override
    public Slice<Feed> getFeedPageByClubId(Long clubId, int size, Long currentCursorId) {
        Slice<Feed> feedPages = feedRepository.findPageByClubIdOrderById(clubId, size + 1, currentCursorId);
        List<Feed> feeds = new ArrayList<>(feedPages.getContent());
        if (feeds.size() == size + 1) {
            feeds.remove(feeds.size() - 1);
            return new SliceImpl<>(feeds, PageRequest.of(feedPages.getNumber(), size), true);
        }
        return feedPages;
    }

    @Override
    public List<Feed> getNewestAll() {
        return feedRepository.findNewestAll();
    }

    @Override
    public Feed getById(Long feedId) {
        return feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFound("Feed(id: " + feedId + ")를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Long create(Feed feed) {
        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }

    @Override
    @Transactional
    public void delete(Feed feed) {
        feedRepository.delete(feed);
    }
}
