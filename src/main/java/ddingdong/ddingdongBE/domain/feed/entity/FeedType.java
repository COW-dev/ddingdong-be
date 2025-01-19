package ddingdong.ddingdongBE.domain.feed.entity;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedType {
  IMAGE(DomainType.FEED_IMAGE),
  VIDEO(DomainType.FEED_VIDEO);

  private final DomainType domainType;
}
