# API 05: 동아리 이달의 피드 현황 조회 (API 7)

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/central/feeds/status?year=&month=` | ROLE_CLUB | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 |
| month | int | Y | 조회 월 (1~12) |

### Response Body
```json
{
  "year": 2025,
  "month": 3,
  "feedCount": 5,
  "totalViewCount": 1200,
  "totalLikeCount": 80,
  "totalCommentCount": 45
}
```

### 비즈니스 규칙
- 로그인한 동아리 회장의 **내 동아리** 해당 월 피드 집계
- 피드가 없으면 모든 카운트 0 반환 (null 반환 아님)
- `feedCount`: 해당 월 내 동아리 피드 수
- `totalViewCount`: 해당 월 내 동아리 피드 전체 조회수 합계
- `totalLikeCount`: 해당 월 내 동아리 피드 전체 좋아요 합계
- `totalCommentCount`: 해당 월 내 동아리 피드 전체 댓글 합계

> **기존 plan과의 차이점**: 기존 plan은 myBestFeed + topFeed 구조였으나,
> Notion 실제 명세는 **단순 집계 값** (feedCount, totalViewCount, totalLikeCount, totalCommentCount).
> URL도 `/server/central/my/feed-rankings/monthly-best` → `/server/central/feeds/status` 로 변경.

---

## 구현할 파일 목록

### 1. `repository/dto/ClubFeedStatusDto.java` (native query projection)
```java
public interface ClubFeedStatusDto {
    Long getFeedCount();
    Long getTotalViewCount();
    Long getTotalLikeCount();
    Long getTotalCommentCount();
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        COUNT(DISTINCT f.id)      AS feedCount,
        COALESCE(SUM(f.view_count), 0)       AS totalViewCount,
        COUNT(DISTINCT fl.id)     AS totalLikeCount,
        COUNT(DISTINCT fc.id)     AS totalCommentCount
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND f.club_id = (SELECT club_id FROM users WHERE id = :userId)
      AND YEAR(f.created_at) = :year
      AND MONTH(f.created_at) = :month
    """, nativeQuery = true)
ClubFeedStatusDto findClubFeedStatus(
        @Param("userId") Long userId,
        @Param("year") int year,
        @Param("month") int month
);
```

### 3. `service/FeedRankingService.java` — 메서드 추가
```java
ClubFeedStatusQuery getClubFeedStatus(Long userId, int year, int month);
```

### 4. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public ClubFeedStatusQuery getClubFeedStatus(Long userId, int year, int month) {
    ClubFeedStatusDto dto = feedRepository.findClubFeedStatus(userId, year, month);
    return ClubFeedStatusQuery.of(year, month, dto);
}
```

### 5. `service/dto/query/ClubFeedStatusQuery.java`
```java
@Builder
public record ClubFeedStatusQuery(
    int year,
    int month,
    long feedCount,
    long totalViewCount,
    long totalLikeCount,
    long totalCommentCount
) {
    public static ClubFeedStatusQuery of(int year, int month, ClubFeedStatusDto dto) {
        return ClubFeedStatusQuery.builder()
                .year(year)
                .month(month)
                .feedCount(dto.getFeedCount() != null ? dto.getFeedCount() : 0L)
                .totalViewCount(dto.getTotalViewCount() != null ? dto.getTotalViewCount() : 0L)
                .totalLikeCount(dto.getTotalLikeCount() != null ? dto.getTotalLikeCount() : 0L)
                .totalCommentCount(dto.getTotalCommentCount() != null ? dto.getTotalCommentCount() : 0L)
                .build();
    }
}
```

### 6. `controller/dto/response/ClubFeedStatusResponse.java`
```java
@Builder
public record ClubFeedStatusResponse(
    int year,
    int month,
    long feedCount,
    long totalViewCount,
    long totalLikeCount,
    long totalCommentCount
) {
    public static ClubFeedStatusResponse from(ClubFeedStatusQuery query) {
        return ClubFeedStatusResponse.builder()
                .year(query.year())
                .month(query.month())
                .feedCount(query.feedCount())
                .totalViewCount(query.totalViewCount())
                .totalLikeCount(query.totalLikeCount())
                .totalCommentCount(query.totalCommentCount())
                .build();
    }
}
```

### 7. `api/ClubFeedApi.java` — 메서드 추가
```java
@Operation(summary = "이달의 피드 현황 조회 API")
@ApiResponse(responseCode = "200", description = "피드 현황 조회 성공",
    content = @Content(schema = @Schema(implementation = ClubFeedStatusResponse.class)))
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/status")
ClubFeedStatusResponse getFeedStatus(
    @AuthenticationPrincipal PrincipalDetails principalDetails,
    @RequestParam("year") @Min(2000) @Max(2100) int year,
    @RequestParam("month") @Min(1) @Max(12) int month
);
```

> **Note**: `ClubFeedApi`의 `@RequestMapping`이 `/server/central/feeds` 이어야 함.

### 8. `controller/ClubFeedController.java` — 메서드 추가
```java
@Override
public ClubFeedStatusResponse getFeedStatus(PrincipalDetails principalDetails,
        int year, int month) {
    return ClubFeedStatusResponse.from(
            feedRankingService.getClubFeedStatus(principalDetails.getId(), year, month));
}
```

---

## 완료 기준
- [ ] GET `/server/central/feeds/status?year=2025&month=3` 200 응답
- [ ] 내 동아리 해당 월 피드 집계 정확
- [ ] 피드 없을 때 모든 카운트 0 반환 (null 아님)
- [ ] CLUB 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `ClubFeedStatusApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 현황 조회 성공 | CLUB 인증, 해당 월 내 동아리 피드 3개 | 200 + feedCount=3 |
| 집계 정확성 | 피드 2개 (viewCount=100, 200), 좋아요 5개, 댓글 3개 | totalViewCount=300, totalLikeCount=5, totalCommentCount=3 |
| 피드 없음 | 해당 월 내 동아리 피드 없음 | feedCount=0, 모든 total=0 |
| 내 동아리만 집계 | 동아리A·B 피드 혼재 | 동아리A 회장은 동아리A 피드만 집계 |
| CLUB 아닌 접근 | 일반 USER 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |
