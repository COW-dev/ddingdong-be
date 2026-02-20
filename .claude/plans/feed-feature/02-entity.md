# Task 02: Entity 생성 및 수정

## Overview
FeedLike, FeedComment 엔티티를 생성하고, Feed 엔티티에 viewCount 필드를 추가한다.

## Dependencies
- Task 01 (DB 마이그레이션)

## Files to Create/Modify

### 1. 생성: `domain/feed/entity/FeedLike.java`

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private FeedLike(Long id, Feed feed, User user) {
        this.id = id;
        this.feed = feed;
        this.user = user;
    }
}
```

**주의사항:**
- FeedLike는 soft delete 대신 **hard delete** 적용 (unique constraint와의 충돌 방지)
- `@SQLDelete`와 `@SQLRestriction` 어노테이션 사용하지 않음
- deleted_at 컬럼 불필요 (마이그레이션 스크립트에서도 제거 고려, 또는 유지 후 미사용)

### 2. 생성: `domain/feed/entity/FeedComment.java`

```java
@Entity
@Getter
@SQLDelete(sql = "update feed_comment set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FeedComment(Long id, Feed feed, User user, String content) {
        this.id = id;
        this.feed = feed;
        this.user = user;
        this.content = content;
    }
}
```

- FixZoneComment 패턴 참조
- soft delete 적용

### 3. 수정: `domain/feed/entity/Feed.java`

viewCount 필드 추가:

```java
// 추가할 필드
@Column(nullable = false)
private Long viewCount = 0L;

// Builder에 viewCount 추가
@Builder
private Feed(Long id, String activityContent, Club club, FeedType feedType, Long viewCount) {
    this.id = id;
    this.activityContent = activityContent;
    this.club = club;
    this.feedType = feedType;
    this.viewCount = viewCount != null ? viewCount : 0L;
}

// 조회수 증가 메서드
public void incrementViewCount() {
    this.viewCount++;
}
```

## Acceptance Criteria
- [ ] FeedLike 엔티티: Feed, User ManyToOne 관계, hard delete 방식
- [ ] FeedComment 엔티티: Feed, User ManyToOne 관계, soft delete 적용
- [ ] Feed 엔티티에 viewCount 필드 추가 (기본값 0)
- [ ] Feed.incrementViewCount() 메서드 존재
- [ ] 모든 엔티티가 BaseEntity 상속 (createdAt, updatedAt)
