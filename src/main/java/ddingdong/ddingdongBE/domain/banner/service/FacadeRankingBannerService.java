package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import java.util.List;

public interface FacadeRankingBannerService {

    void createRankingBanners(List<FeedMonthlyRanking> firstPlaceRankings);
}
