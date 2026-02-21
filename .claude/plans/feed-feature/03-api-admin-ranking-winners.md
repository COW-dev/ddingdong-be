# API 03: 총동연 역대 피드 1위 조회 (API 9)

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/admin/feeds/ranking/last?year=` | ROLE_ADMIN | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 (예: 2025) |

### Response Body
**단건** 응답 (월별 목록이 아님)
```json
{
  "feedId": 5,
  "thumbnailUrl": "https://...",
  "clubName": "동아리명",
  "score": 300,
  "viewCount": 200,
  "likeCount": 30,
  "commentCount": 14,
  "targetYear": 2025,
  "targetMonth": 8
}
```

### 비즈니스 규칙
- 해당 `year`의 **지난 연도** 전체 피드 중 절대 1위 피드 단건 반환
  - 예: `year=2025` → 2025년 전체 기간 피드 중 score 최고 단건
  - 단, 현재 달(이번 달) 피드는 제외
- 동점 시 feedId ASC 기준으로 1위 결정
- 피드가 없으면 `null` 또는 404 반환

> **기존 plan과의 차이점**: 기존 plan은 월별 1위 목록(최대 12개)을 반환했으나,
> Notion 실제 명세는 **해당 연도 전체 1위 단건**을 반환.
> `targetYear`, `targetMonth` 필드로 해당 피드가 몇 년 몇 월 기준인지 표시.

---

## 구현할 파일 목록

### 1. `repository/dto/FeedRankingWinnerDto.java` (native query projection)
```java
public interface FeedRankingWinnerDto {
    Long getFeedId();
    String getThumbnailUrl();
    String getClubName();
    Long getScore();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Integer getTargetYear();
    Integer getTargetMonth();
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        f.id              AS feedId,
        f.thumbnail_url   AS thumbnailUrl,
        c.name            AS clubName,
        (f.view_count * 1 + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) AS score,
        f.view_count      AS viewCount,
        COUNT(DISTINCT fl.id) AS likeCount,
        COUNT(DISTINCT fc.id) AS commentCount,
        YEAR(f.created_at)    AS targetYear,
        MONTH(f.created_at)   AS targetMonth
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND YEAR(f.created_at) = :year
      AND NOT (YEAR(f.created_at) = YEAR(CURRENT_DATE()) AND MONTH(f.created_at) = MONTH(CURRENT_DATE()))
    GROUP BY f.id, f.thumbnail_url, c.name, f.view_count, YEAR(f.created_at), MONTH(f.created_at)
    ORDER BY score DESC, f.id ASC
    LIMIT 1
    """, nativeQuery = true)
Optional<FeedRankingWinnerDto> findYearlyWinner(@Param("year") int year);
```

> **Note**: CTE 없이 단순 LIMIT 1로 절대 1위 단건 조회.
> 현재 달 제외 조건: `NOT (현재 연도 AND 현재 월)` — 과거 연도 조회 시에도 안전하게 동작.

### 3. `service/FeedRankingService.java` — 메서드 추가
```java
Optional<FeedRankingWinnerQuery> getYearlyWinner(int year);
```

### 4. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public Optional<FeedRankingWinnerQuery> getYearlyWinner(int year) {
    return feedRepository.findYearlyWinner(year)
            .map(FeedRankingWinnerQuery::from);
}
```

### 5. `service/dto/query/FeedRankingWinnerQuery.java`
```java
@Builder
public record FeedRankingWinnerQuery(
    Long feedId,
    String thumbnailUrl,
    String clubName,
    Long score,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    int targetYear,
    int targetMonth
) {
    public static FeedRankingWinnerQuery from(FeedRankingWinnerDto dto) {
        return FeedRankingWinnerQuery.builder()
                .feedId(dto.getFeedId())
                .thumbnailUrl(dto.getThumbnailUrl())
                .clubName(dto.getClubName())
                .score(dto.getScore())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .targetYear(dto.getTargetYear())
                .targetMonth(dto.getTargetMonth())
                .build();
    }
}
```

### 6. `controller/dto/response/AdminFeedRankingWinnerResponse.java`
```java
@Builder
public record AdminFeedRankingWinnerResponse(
    Long feedId,
    String thumbnailUrl,
    String clubName,
    Long score,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    int targetYear,
    int targetMonth
) {
    public static AdminFeedRankingWinnerResponse from(FeedRankingWinnerQuery query) {
        return AdminFeedRankingWinnerResponse.builder()
                .feedId(query.feedId())
                .thumbnailUrl(query.thumbnailUrl())
                .clubName(query.clubName())
                .score(query.score())
                .viewCount(query.viewCount())
                .likeCount(query.likeCount())
                .commentCount(query.commentCount())
                .targetYear(query.targetYear())
                .targetMonth(query.targetMonth())
                .build();
    }
}
```

### 7. `api/AdminFeedApi.java` — 메서드 추가
```java
@Operation(summary = "역대 피드 1위 조회 API")
@ApiResponse(responseCode = "200", description = "역대 1위 피드 조회 성공",
    content = @Content(schema = @Schema(implementation = AdminFeedRankingWinnerResponse.class)))
@ApiResponse(responseCode = "404", description = "해당 연도 피드 없음")
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/ranking/last")
AdminFeedRankingWinnerResponse getYearlyWinner(
    @RequestParam("year") @Min(2000) @Max(2100) int year
);
```

### 8. `controller/AdminFeedController.java` — 메서드 추가
```java
@Override
public AdminFeedRankingWinnerResponse getYearlyWinner(int year) {
    return feedRankingService.getYearlyWinner(year)
            .map(AdminFeedRankingWinnerResponse::from)
            .orElseThrow(FeedException.FeedRankingNotFoundException::new);
}
```

### 9. 예외 추가
```java
public static final class FeedRankingNotFoundException extends FeedException {
    private static final String MESSAGE = "해당 연도에 피드가 존재하지 않습니다.";
    public FeedRankingNotFoundException() { super(MESSAGE, NOT_FOUND.value()); }
}
```

---

## 완료 기준
- [ ] GET `/server/admin/feeds/ranking/last?year=2025` 200 응답 (단건)
- [ ] score = viewCount×1 + likeCount×3 + commentCount×5 최고 피드 반환
- [ ] 동점 시 feedId ASC 기준 1위 결정
- [ ] 현재 달 피드 결과에 미포함
- [ ] 피드 없을 때 404 응답
- [ ] ADMIN 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `AdminFeedRankingApiTest` (통합, 기존 클래스에 추가)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 역대 1위 조회 성공 | ADMIN 인증, year=2025, 여러 피드 존재 | 200 + 단건 응답 |
| score 최고 피드 반환 | 3개 피드, score 각각 50/100/80 | score=100인 피드 반환 |
| 동점 시 feedId ASC | 동일 score 피드 2개 (feedId=3, feedId=7) | feedId=3 반환 |
| 현재 달 미포함 | 현재 달 피드가 최고 score | 현재 달 피드 제외, 차순위 반환 |
| 피드 없음 | 해당 연도 피드 없음 | 404 |
| ADMIN 아닌 접근 | ROLE_CLUB 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |
