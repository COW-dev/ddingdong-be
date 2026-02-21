package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.common.exception.FeedException.FeedRankingNotFoundException;
import ddingdong.ddingdongBE.domain.feed.api.AdminFeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminFeedRankingWinnerResponse;
import ddingdong.ddingdongBE.domain.feed.service.FeedRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminFeedController implements AdminFeedApi {

    private final FeedRankingService feedRankingService;

    @Override
    public AdminFeedRankingWinnerResponse getYearlyWinner(int year) {
        return feedRankingService.getYearlyWinner(year)
                .map(AdminFeedRankingWinnerResponse::from)
                .orElseThrow(FeedRankingNotFoundException::new);
    }
}
