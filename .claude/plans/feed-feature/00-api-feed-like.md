# API 00: 피드 좋아요 생성/취소 ✅ 완료

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| POST | `/server/feeds/{feedId}/likes` | USER | 201 |
| DELETE | `/server/feeds/{feedId}/likes` | USER | 204 |

---

## 구현 완료 파일

### `entity/FeedLike.java`
- `Feed`, `User` ManyToOne (LAZY)
- hard delete 방식 (soft delete 미적용 — unique constraint 충돌 이슈)
- `@Builder` on constructor, `private` 접근자

### `repository/FeedLikeRepository.java`
```java
boolean existsByFeedIdAndUserId(Long feedId, Long userId);
void deleteByFeedIdAndUserId(Long feedId, Long userId);
long countByFeedId(Long feedId);
```

### `service/FeedLikeService.java` (인터페이스)
```java
void create(Long feedId, Long userId);
void delete(Long feedId, Long userId);
long countByFeedId(Long feedId);
boolean existsByFeedIdAndUserId(Long feedId, Long userId);
```

### `service/GeneralFeedLikeService.java`
- `create`: 중복 좋아요 시 `FeedException.DuplicatedFeedLikeException` (400)
- `delete`: `deleteByFeedIdAndUserId` 직접 호출 (없으면 no-op)

### `api/FeedLikeApi.java`
```
POST  /server/feeds/{feedId}/likes   → 201
DELETE /server/feeds/{feedId}/likes  → 204
```

### `controller/FeedLikeController.java`
- `createLike`: `feedLikeService.create(feedId, principalDetails.getUser().getId())`
- `deleteLike`: `feedLikeService.delete(feedId, principalDetails.getUser().getId())`

### Flyway
- `V54__create_feed_like_table.sql` — feed_like 테이블 생성

---

## 예외
```java
// FeedException.java 내
public static final class DuplicatedFeedLikeException extends FeedException {
    private static final String MESSAGE = "이미 좋아요한 피드입니다.";
    public DuplicatedFeedLikeException() { super(MESSAGE, BAD_REQUEST.value()); }
}
```

---

## 테스트 케이스

### `FeedLikeApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 좋아요 생성 성공 | 인증 사용자, 존재하는 피드, 최초 좋아요 | 201 |
| 좋아요 중복 생성 | 이미 좋아요한 피드에 재요청 | 400 + DuplicatedFeedLikeException |
| 좋아요 생성 — 미인증 | Authorization 헤더 없음 | 401 |
| 좋아요 생성 — 존재하지 않는 피드 | feedId 없음 | 404 |
| 좋아요 취소 성공 | 인증 사용자, 본인이 좋아요한 피드 | 204 |
| 좋아요 취소 — 미인증 | Authorization 헤더 없음 | 401 |

---

## 커밋
- `[DDING-000] 피드 좋아요 생성 API 구현` (8c58ab9)
- `[DDING-000] 피드 좋아요 취소 API 구현` (c713ee4)
