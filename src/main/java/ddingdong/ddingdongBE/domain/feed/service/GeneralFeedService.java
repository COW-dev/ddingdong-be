package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.repository.FeedRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return buildSlice(feedPages, size);
    }
    @Override
    public Slice<Feed> getNewestFeedPerClubPage(int size, Long currentCursorId) {
        Slice<Feed> feedPages = feedRepository.findNewestPerClubPage(size + 1, currentCursorId);
        return buildSlice(feedPages, size);
    }

    @Override
    public Feed getById(Long feedId) {
        return feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFound("Feed(id: " + feedId + ")를 찾을 수 없습니다."));
    }

    @Override
    public Optional<Feed> findById(Long feedId) {
        return feedRepository.findById(feedId);
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

    private Slice<Feed> buildSlice(Slice<Feed> originalSlice, int size) {
        List<Feed> content = new ArrayList<>(originalSlice.getContent());
        if (content.isEmpty()) {
            return null;
        }

        boolean hasNext = content.size() > size;

        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, PageRequest.of(originalSlice.getNumber(), size), hasNext);
    }
}
