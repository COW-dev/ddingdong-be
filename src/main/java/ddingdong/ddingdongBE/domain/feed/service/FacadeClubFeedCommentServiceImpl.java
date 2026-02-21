package ddingdong.ddingdongBE.domain.feed.service;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacadeClubFeedCommentServiceImpl implements FacadeClubFeedCommentService {

    private final FeedCommentService feedCommentService;
    private final FeedService feedService;
    private final ClubService clubService;

    @Override
    @Transactional
    public void forceDelete(User user, Long feedId, Long commentId) {
        Club club = clubService.getByUserId(user.getId());
        Feed feed = feedService.getById(feedId);
        feedCommentService.forceDelete(feed, club, commentId);
    }
}
