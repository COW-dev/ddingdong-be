package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.FeedLikeApi;
import ddingdong.ddingdongBE.domain.feed.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class FeedLikeController implements FeedLikeApi {

    private final FeedLikeService feedLikeService;

    @Override
    public void createLike(Long feedId, String uuid) {
        feedLikeService.create(feedId, uuid);
    }

    @Override
    public void deleteLike(Long feedId, String uuid) {
        feedLikeService.delete(feedId, uuid);
    }
}
