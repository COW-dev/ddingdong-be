package ddingdong.ddingdongBE.domain.feed.controller;

import ddingdong.ddingdongBE.domain.feed.api.AdminFeedApi;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminClubFeedRankingResponse;
import ddingdong.ddingdongBE.domain.feed.controller.dto.response.AdminFeedRankingWinnerResponse;
import ddingdong.ddingdongBE.domain.feed.service.FeedRankingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminFeedController implements AdminFeedApi {

    private final FeedRankingService feedRankingService;

    @Override
    public List<AdminFeedRankingWinnerResponse> getMonthlyWinners(int year) {
        return feedRankingService.getMonthlyWinners(year).stream()
                .map(AdminFeedRankingWinnerResponse::from)
                .toList();
    }

    @Override
    public List<AdminClubFeedRankingResponse> getClubFeedRanking(int year, int month) {
        return AdminClubFeedRankingResponse.from(
                feedRankingService.getClubFeedRanking(year, month)
        );
    }
}
