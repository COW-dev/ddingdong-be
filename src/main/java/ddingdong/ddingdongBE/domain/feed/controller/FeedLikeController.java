package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedLikeApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.request.CreateFeedLikeRequest;
import ddingdong.ddingdongBE.domain.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedLikeController implements FeedLikeApi {

    private final FeedService feedService;

    @Override
    public void createLike(Long feedId, CreateFeedLikeRequest request) {
        feedService.addLikeCount(feedId, request.count());
    }
}
