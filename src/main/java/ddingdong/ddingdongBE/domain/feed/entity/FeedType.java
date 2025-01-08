package ddingdong.ddingdongBE.domain.feed.entity;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedType {
  IMAGE(DomainType.FEED_IMAGE),
  VIDEO(DomainType.FEED_VIDEO);

  private final DomainType domainType;

  public static FeedType findByContentType(String contentType) {
    return Arrays.stream(values())
        .filter(feedType -> feedType.name().equalsIgnoreCase(contentType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Feed 내 해당 컨텐츠 종류(" + contentType + ")는 지원하지 않습니다."));
  }
}
