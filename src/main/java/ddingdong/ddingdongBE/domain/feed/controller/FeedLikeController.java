package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.feed.api.FeedLikeApi;
import ddingdong.ddingdongBE.domain.feed.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedLikeController implements FeedLikeApi {

    private final FeedLikeService feedLikeService;

    @Override
    public void createLike(Long feedId, PrincipalDetails principalDetails) {
        feedLikeService.create(feedId, principalDetails.getUser().getId());
    }


}
