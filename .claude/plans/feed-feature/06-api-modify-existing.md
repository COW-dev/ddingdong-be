# API 06: 기존 피드 조회 API 수정 (likeCount/commentCount 추가)

## 수정 대상 API

| Method | URL | 추가 필드 |
|--------|-----|---------|
| GET | `/server/feeds` | `likeCount`, `commentCount` (목록 아이템별) |
| GET | `/server/feeds/{feedId}` | `likeCount`, `commentCount`, `comments[]` |
| GET | `/server/clubs/{clubId}/feeds` | `likeCount`, `commentCount` (목록 아이템별) |

### 추가되는 Response 필드

**목록 API 아이템**
```json
{
  "likeCount": 30,
  "commentCount": 20
}
```

**상세 API**
```json
{
  "likeCount": 30,
  "commentCount": 20,
  "comments": [
    {
      "id": 1,
      "content": "댓글 내용",
      "authorName": "홍길동",
      "createdAt": "2025-03-15T10:00:00"
    }
  ]
}
```

---

## 구현할 파일 목록

### 1. `repository/FeedCommentRepository.java` — 쿼리 추가
```java
List<FeedComment> findAllByFeedId(Long feedId);
long countByFeedId(Long feedId);
```

### 2. `service/FeedCommentService.java` — 메서드 추가
```java
List<FeedCommentQuery> getAllByFeedId(Long feedId);
long countByFeedId(Long feedId);
```

### 3. `service/GeneralFeedCommentService.java` — 메서드 추가
```java
@Override
public List<FeedCommentQuery> getAllByFeedId(Long feedId) {
    return feedCommentRepository.findAllByFeedId(feedId)
            .stream()
            .map(FeedCommentQuery::from)
            .toList();
}

@Override
public long countByFeedId(Long feedId) {
    return feedCommentRepository.countByFeedId(feedId);
}
```

### 4. `service/dto/query/FeedCommentQuery.java` (신규)
```java
@Builder
public record FeedCommentQuery(
    Long id,
    String content,
    String authorName,
    LocalDateTime createdAt
) {
    public static FeedCommentQuery from(FeedComment comment) {
        return FeedCommentQuery.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
```

### 5. 기존 Query DTO 수정

**`service/dto/query/FeedListQuery.java`** — 필드 추가
```java
@Builder
public record FeedListQuery(
    // 기존 필드들...
    long likeCount,      // 추가
    long commentCount    // 추가
) { }
```

**`service/dto/query/FeedQuery.java`** — 필드 추가
```java
@Builder
public record FeedQuery(
    // 기존 필드들...
    long likeCount,                    // 추가
    long commentCount,                 // 추가
    List<FeedCommentQuery> comments    // 추가
) { }
```

### 6. Facade Service 수정

**`service/FacadeFeedServiceImpl.java`** (또는 구현체) — likeCount/commentCount 조회 추가
```java
// 목록 조회 시: 각 Feed에 대해 likeCount, commentCount 조회 후 FeedListQuery 생성
// 상세 조회 시: likeCount, commentCount, comments 조회 후 FeedQuery 생성
// FeedLikeService, FeedCommentService 의존성 추가 필요
```

> **성능 주의**: N+1 문제 발생 가능. 목록 조회 시 feedId 목록으로 일괄 조회하거나
> native query에서 COUNT 서브쿼리로 한 번에 가져오는 방식 고려.

### 7. 기존 Response DTO 수정

**`controller/dto/response/FeedPageResponse.java`** — 아이템에 likeCount, commentCount 추가

**`controller/dto/response/ClubFeedPageResponse.java`** — 아이템에 likeCount, commentCount 추가

**`controller/dto/response/FeedResponse.java`** — likeCount, commentCount, comments 추가

---

## 성능 고려사항

목록 API에서 N+1 방지 방법 (선택):

**옵션 A: 서비스 레이어에서 Map으로 일괄 조회**
```java
List<Long> feedIds = feeds.stream().map(Feed::getId).toList();
Map<Long, Long> likeCounts = feedLikeRepository.countByFeedIds(feedIds);
Map<Long, Long> commentCounts = feedCommentRepository.countByFeedIds(feedIds);
```

**옵션 B: native query에 COUNT 서브쿼리 추가**
기존 `findPageByClubIdOrderById`, `getAllFeedPage` 쿼리에 LEFT JOIN COUNT 추가

→ **옵션 A 권장** (쿼리 변경 최소화)

---

## 하위 호환성 확인
- 기존 필드 제거 없음 (likeCount, commentCount, comments 필드 추가만)
- 기존 클라이언트는 새 필드 무시 → 하위 호환 유지

---

## 완료 기준
- [ ] GET `/server/feeds` 응답 아이템에 likeCount, commentCount 포함
- [ ] GET `/server/feeds/{feedId}` 응답에 likeCount, commentCount, comments[] 포함
- [ ] GET `/server/clubs/{clubId}/feeds` 응답 아이템에 likeCount, commentCount 포함
- [ ] comments 배열에 id, content, authorName, createdAt 포함
- [ ] 기존 필드 변경 없이 필드 추가만 (하위 호환)
- [ ] N+1 없이 효율적 쿼리

---

## 테스트 케이스

### `FeedApiTest` (통합, 기존 테스트 수정)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 전체 피드 목록 — likeCount | 피드에 좋아요 2개 | 응답 아이템에 `likeCount=2` |
| 전체 피드 목록 — commentCount | 피드에 댓글 3개 | 응답 아이템에 `commentCount=3` |
| 피드 상세 — likeCount/commentCount | 좋아요 2, 댓글 3 | 각 필드 정확 |
| 피드 상세 — comments 배열 | 댓글 2개 존재 | `comments.size()=2` |
| comments 필드 구조 | 댓글 1개 | id, content, authorName, createdAt 포함 |
| 동아리별 피드 목록 — likeCount | 피드에 좋아요 1개 | 응답 아이템에 `likeCount=1` |
| soft delete 댓글 미포함 | 삭제된 댓글 1개, 정상 댓글 1개 | `commentCount=1` |
