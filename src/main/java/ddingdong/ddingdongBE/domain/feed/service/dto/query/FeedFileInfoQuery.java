package ddingdong.ddingdongBE.domain.feed.service.dto.query;

public record FeedFileInfoQuery(
    String id,
    String originUrl,
    String cdnUrl,
    String fileName
) {

    public static FeedFileInfoQuery of(String id, String originUrl, String cdnUrl, String fileName) {
        return new FeedFileInfoQuery(id, originUrl, cdnUrl, fileName);
    }

}
