package ddingdong.ddingdongBE.domain.feed.service.dto.query;

public record FeedFileUrlQuery(
    String id,
    String originUrl,
    String cdnUrl
) {

    public static FeedFileUrlQuery of(String id, String originUrl, String cdnUrl) {
        return new FeedFileUrlQuery(id, originUrl, cdnUrl);
    }

}
