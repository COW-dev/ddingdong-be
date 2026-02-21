# API 04: 동아리 피드 랭킹 조회 (API 6)

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/central/feeds/ranking?year=&month=` | ROLE_CLUB | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 |
| month | int | Y | 조회 월 (1~12) |

### Response Body
```json
[
  {
    "rank": 1,
    "feedId": 10,
    "thumbnailUrl": "https://...",
    "clubName": "동아리명",
    "score": 150,
    "viewCount": 80,
    "likeCount": 20,
    "commentCount": 10
  }
]
```

### 비즈니스 규칙
- **전체 동아리** 피드를 포함한 랭킹 (내 동아리만 필터하지 않음)
- 해당 연도/월 pieds에 대해 랭킹 계산
- 랭킹 공식: `score = viewCount×1 + likeCount×3 + commentCount×5`
- 정렬: score DESC, feedId ASC (동점 시)

> **기존 plan과의 차이점**: 기존 URL `/server/central/my/feed-rankings?year=` 에서
> `/server/central/feeds/ranking?year=&month=` 으로 변경.
> month 파라미터 추가. 내 클럽만 필터하는 로직 제거 (전체 랭킹 표시).

---

## 구현할 파일 목록

### 1. `repository/dto/ClubFeedRankingDto.java` (native query projection)
```java
public interface ClubFeedRankingDto {
    Long getFeedId();
    String getThumbnailUrl();
    String getClubName();
    Long getScore();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    WITH global_ranked AS (
        SELECT
            f.id              AS feedId,
            f.thumbnail_url   AS thumbnailUrl,
            c.name            AS clubName,
            f.view_count      AS viewCount,
            COUNT(DISTINCT fl.id) AS likeCount,
            COUNT(DISTINCT fc.id) AS commentCount,
            (f.view_count * 1 + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) AS score,
            RANK() OVER (
                ORDER BY (f.view_count * 1 + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) DESC, f.id ASC
            ) AS rankPos
        FROM feed f
        JOIN club c ON f.club_id = c.id
        LEFT JOIN feed_like fl ON fl.feed_id = f.id
        LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
        WHERE f.deleted_at IS NULL
          AND YEAR(f.created_at) = :year
          AND MONTH(f.created_at) = :month
        GROUP BY f.id, f.thumbnail_url, c.name, f.view_count
    )
    SELECT feedId, thumbnailUrl, clubName, score, viewCount, likeCount, commentCount, rankPos
    FROM global_ranked
    ORDER BY rankPos ASC
    """, nativeQuery = true)
List<ClubFeedRankingDto> findClubFeedRanking(
        @Param("year") int year,
        @Param("month") int month
);
```

> **Note**: CTE로 전체 피드 랭킹을 계산. 전체 동아리 포함.
> 기존 plan의 `userId` 파라미터 제거 (내 클럽 필터 없음).

### 3. `service/FeedRankingService.java` — 메서드 추가
```java
List<ClubFeedRankingQuery> getClubFeedRanking(int year, int month);
```

### 4. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public List<ClubFeedRankingQuery> getClubFeedRanking(int year, int month) {
    return feedRepository.findClubFeedRanking(year, month)
            .stream()
            .map(ClubFeedRankingQuery::from)
            .toList();
}
```

### 5. `service/dto/query/ClubFeedRankingQuery.java`
```java
@Builder
public record ClubFeedRankingQuery(
    long rank,
    Long feedId,
    String thumbnailUrl,
    String clubName,
    Long score,
    Long viewCount,
    Long likeCount,
    Long commentCount
) {
    public static ClubFeedRankingQuery from(ClubFeedRankingDto dto) {
        return ClubFeedRankingQuery.builder()
                .rank(dto.getRankPos())
                .feedId(dto.getFeedId())
                .thumbnailUrl(dto.getThumbnailUrl())
                .clubName(dto.getClubName())
                .score(dto.getScore())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .build();
    }
}
```

### 6. `controller/dto/response/ClubFeedRankingResponse.java`
```java
@Builder
public record ClubFeedRankingResponse(
    long rank,
    Long feedId,
    String thumbnailUrl,
    String clubName,
    Long score,
    Long viewCount,
    Long likeCount,
    Long commentCount
) {
    public static List<ClubFeedRankingResponse> from(List<ClubFeedRankingQuery> queries) {
        return queries.stream()
                .map(q -> ClubFeedRankingResponse.builder()
                        .rank(q.rank())
                        .feedId(q.feedId())
                        .thumbnailUrl(q.thumbnailUrl())
                        .clubName(q.clubName())
                        .score(q.score())
                        .viewCount(q.viewCount())
                        .likeCount(q.likeCount())
                        .commentCount(q.commentCount())
                        .build())
                .toList();
    }
}
```

### 7. `api/ClubFeedApi.java` — 메서드 추가
```java
@Operation(summary = "피드 랭킹 조회 API")
@ApiResponse(responseCode = "200", description = "피드 랭킹 조회 성공",
    content = @Content(array = @ArraySchema(
        schema = @Schema(implementation = ClubFeedRankingResponse.class))))
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/ranking")
List<ClubFeedRankingResponse> getFeedRanking(
    @RequestParam("year") @Min(2000) @Max(2100) int year,
    @RequestParam("month") @Min(1) @Max(12) int month
);
```

> **Note**: `ClubFeedApi`의 `@RequestMapping`이 `/server/central/feeds`로 변경되어야 함.
> 기존이 `/server/central/my/feeds`였다면 URL 조정 필요.

### 8. `controller/ClubFeedController.java` — 메서드 추가
```java
@Override
public List<ClubFeedRankingResponse> getFeedRanking(int year, int month) {
    return ClubFeedRankingResponse.from(
            feedRankingService.getClubFeedRanking(year, month));
}
```

---

## 완료 기준
- [ ] GET `/server/central/feeds/ranking?year=2025&month=3` 200 응답
- [ ] 전체 동아리 피드 랭킹 반환 (내 동아리만 필터 아님)
- [ ] score = viewCount×1 + likeCount×3 + commentCount×5 정확히 계산
- [ ] score DESC 정렬, 동점 시 feedId ASC
- [ ] 피드 없으면 빈 배열 반환
- [ ] CLUB 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `ClubFeedRankingApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 랭킹 조회 성공 | CLUB 인증, year=2025, month=3 | 200 + 배열 |
| 전체 동아리 포함 | 동아리A·B 피드 혼재 | 모든 동아리 피드 포함 |
| score 계산 검증 | viewCount=10, likeCount=5, commentCount=3 | score=40 |
| score DESC 정렬 | 여러 피드 존재 | 첫 번째가 최고 score |
| 피드 없음 | 빈 달 조회 | 200 + `[]` |
| CLUB 아닌 접근 | 일반 USER 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |
