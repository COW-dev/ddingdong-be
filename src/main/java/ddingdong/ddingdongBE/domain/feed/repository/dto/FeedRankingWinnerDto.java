package ddingdong.ddingdongBE.domain.feed.repository.dto;

public interface FeedRankingWinnerDto {

    String getClubName();

    Long getFeedCount();

    Long getViewCount();

    Long getLikeCount();

    Long getCommentCount();

    Long getScore();

    Integer getTargetYear();

    Integer getTargetMonth();
}
