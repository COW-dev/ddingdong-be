package ddingdong.ddingdongBE.domain.feed.repository.dto;

public interface MonthlyFeedRankingDto {

    Long getClubId();

    String getClubName();

    Long getFeedCount();

    Long getViewCount();

    Long getLikeCount();

    Long getCommentCount();

    Long getScore();
}
