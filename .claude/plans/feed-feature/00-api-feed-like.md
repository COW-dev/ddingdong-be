# API 00: 피드 좋아요 생성/취소 (API 1~2)

## API 명세

| No | Method | URL | Auth | 상태코드 |
|----|--------|-----|------|---------|
| 1  | POST   | `/server/feeds/{feedId}/likes` | 비회원 (X-Anonymous-UUID) | 204 |
| 2  | DELETE | `/server/feeds/{feedId}/likes` | 비회원 (X-Anonymous-UUID) | 204 |

### Request Header
```
X-Anonymous-UUID: {uuid-v4}   // 필수, UUID v4 형식
```

### 비즈니스 규칙
- 인증 없이 UUID 헤더로 식별 (비회원 허용)
- UUID v4 형식 검증, 실패 시 400
- 좋아요 중복 시 409 Conflict (기존 400 → 409로 변경)
- 좋아요 취소: 존재하지 않으면 404

---

## DB 변경사항

### Flyway 마이그레이션 (신규)
`resources/db/migration/V{next}__alter_feed_like_uuid.sql`

```sql
ALTER TABLE feed_like
    DROP FOREIGN KEY fk_feed_like_user,
    DROP COLUMN user_id,
    ADD COLUMN uuid VARCHAR(36) NOT NULL AFTER feed_id;

CREATE UNIQUE INDEX uidx_feed_like_feed_uuid ON feed_like (feed_id, uuid);
```

> **Note**: 기존 user_id 컬럼 제거 → uuid 컬럼으로 대체. hard delete 방식 유지.

---

## 구현할 파일 목록

### 1. `entity/FeedLike.java` (수정)
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// soft delete 미적용 — unique constraint 충돌 이슈로 hard delete 유지
public class FeedLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Column(nullable = false, length = 36)
    private String uuid;   // user_id → uuid 로 교체

    @Builder
    private FeedLike(Feed feed, String uuid) {
        this.feed = feed;
        this.uuid = uuid;
    }
}
```

### 2. `repository/FeedLikeRepository.java` (수정)
```java
boolean existsByFeedIdAndUuid(Long feedId, String uuid);
void deleteByFeedIdAndUuid(Long feedId, String uuid);
long countByFeedId(Long feedId);
```

### 3. `service/FeedLikeService.java` (인터페이스) (수정)
```java
public interface FeedLikeService {
    void create(Long feedId, String uuid);
    void delete(Long feedId, String uuid);
    long countByFeedId(Long feedId);
    boolean existsByFeedIdAndUuid(Long feedId, String uuid);
}
```

### 4. `service/GeneralFeedLikeService.java` (수정)
```java
@Override
@Transactional
public void create(Long feedId, String uuid) {
    // 1. Caffeine 캐시에서 중복 확인 (캐시 미스 시 DB fallback)
    if (isAlreadyLiked(feedId, uuid)) {
        throw new FeedException.DuplicatedFeedLikeException();
    }
    Feed feed = feedService.getById(feedId);
    feedLikeRepository.save(FeedLike.builder().feed(feed).uuid(uuid).build());
    // 2. 캐시에 추가
    cacheService.addLike(uuid, feedId);
}

@Override
@Transactional
public void delete(Long feedId, String uuid) {
    if (!existsByFeedIdAndUuid(feedId, uuid)) {
        throw new FeedException.FeedLikeNotFoundException();
    }
    feedLikeRepository.deleteByFeedIdAndUuid(feedId, uuid);
    cacheService.removeLike(uuid, feedId);
}
```

### 5. `service/FeedLikeCacheService.java` (신규)
```java
@Service
public class FeedLikeCacheService {

    private final Cache<String, Set<Long>> likeCache;

    public FeedLikeCacheService() {
        this.likeCache = Caffeine.newBuilder()
                .expireAfterWrite(14, TimeUnit.DAYS)
                .build();
    }

    public boolean isLiked(String uuid, Long feedId) {
        Set<Long> feedIds = likeCache.getIfPresent(uuid);
        return feedIds != null && feedIds.contains(feedId);
    }

    public void addLike(String uuid, Long feedId) {
        likeCache.asMap()
                .computeIfAbsent(uuid, k -> Collections.synchronizedSet(new HashSet<>()))
                .add(feedId);
    }

    public void removeLike(String uuid, Long feedId) {
        Set<Long> feedIds = likeCache.getIfPresent(uuid);
        if (feedIds != null) feedIds.remove(feedId);
    }
}
```

### 6. `common/filter/UuidValidationFilter.java` (또는 ArgumentResolver)
```java
// X-Anonymous-UUID 헤더 UUID v4 검증
// 형식: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
// 실패 시 400 반환
private static final Pattern UUID_V4_PATTERN =
    Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
```

### 7. `api/FeedLikeApi.java` (수정)
```java
@Tag(name = "Feed Like", description = "피드 좋아요 API")
@RequestMapping("/server/feeds")
public interface FeedLikeApi {

    @Operation(summary = "피드 좋아요 API")
    @ApiResponse(responseCode = "204", description = "좋아요 성공")
    @ApiResponse(responseCode = "409", description = "이미 좋아요한 피드")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{feedId}/likes")
    void createLike(
        @PathVariable("feedId") Long feedId,
        @RequestHeader("X-Anonymous-UUID") String uuid
    );

    @Operation(summary = "피드 좋아요 취소 API")
    @ApiResponse(responseCode = "204", description = "좋아요 취소 성공")
    @ApiResponse(responseCode = "404", description = "좋아요 기록 없음")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{feedId}/likes")
    void deleteLike(
        @PathVariable("feedId") Long feedId,
        @RequestHeader("X-Anonymous-UUID") String uuid
    );
}
```

### 8. `controller/FeedLikeController.java` (수정)
```java
@Override
public void createLike(Long feedId, String uuid) {
    feedLikeService.create(feedId, uuid);
}

@Override
public void deleteLike(Long feedId, String uuid) {
    feedLikeService.delete(feedId, uuid);
}
```

---

## 예외
```java
// FeedException.java 내 추가/수정
public static final class DuplicatedFeedLikeException extends FeedException {
    private static final String MESSAGE = "이미 좋아요한 피드입니다.";
    public DuplicatedFeedLikeException() { super(MESSAGE, CONFLICT.value()); }  // 409
}

public static final class FeedLikeNotFoundException extends FeedException {
    private static final String MESSAGE = "좋아요 기록이 존재하지 않습니다.";
    public FeedLikeNotFoundException() { super(MESSAGE, NOT_FOUND.value()); }    // 404
}
```

---

## 완료 기준
- [ ] POST `/server/feeds/{feedId}/likes` 204 응답 (UUID 헤더 포함)
- [ ] DELETE `/server/feeds/{feedId}/likes` 204 응답
- [ ] 좋아요 중복 시 409 응답
- [ ] 좋아요 없는 상태에서 취소 시 404 응답
- [ ] UUID 형식 오류 시 400 응답
- [ ] 비인증 요청 허용 (401 미발생)
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `FeedLikeApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 좋아요 생성 성공 | 유효 UUID, 존재하는 피드 | 204 |
| 좋아요 중복 | 동일 UUID로 재요청 | 409 |
| 좋아요 생성 — UUID 없음 | X-Anonymous-UUID 헤더 없음 | 400 |
| 좋아요 생성 — UUID 형식 오류 | `not-a-uuid` | 400 |
| 좋아요 생성 — 존재하지 않는 피드 | feedId 없음 | 404 |
| 좋아요 취소 성공 | 좋아요한 UUID | 204 |
| 좋아요 취소 — 기록 없음 | 좋아요 안 한 UUID | 404 |
| 인증 없이 접근 가능 | Authorization 헤더 없음 | 204 (성공) |
