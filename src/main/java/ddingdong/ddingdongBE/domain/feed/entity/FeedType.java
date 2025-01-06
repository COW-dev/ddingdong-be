package ddingdong.ddingdongBE.domain.feed.entity;

import java.util.Arrays;

public enum FeedType {
  IMAGE, VIDEO;

  public static FeedType findByContentType(String contentType) {
    return Arrays.stream(values())
        .filter(feedType -> feedType.name().equalsIgnoreCase(contentType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Feed 내 해당 컨텐츠 종류(" + contentType + ")는 지원하지 않습니다."));
  }
}
