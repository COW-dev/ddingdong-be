# API 03: 총동연 지난 피드 랭킹 1위 목록 조회

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/admin/feed-rankings/winners?year=` | ADMIN | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 (예: 2025) |

### Response Body
```json
{
  "winners": [
    {
      "month": 1,
      "clubName": "동아리명",
      "feedId": 5,
      "activityContent": "피드 내용",
      "feedType": "IMAGE",
      "totalScore": 200,
      "viewCount": 120,
      "likeCount": 50,
      "commentCount": 30
    }
  ]
}
```

### 비즈니스 규칙
- 해당 연도의 각 월별 1위 피드를 반환 (총 최대 12개)
- 이미 지난 달만 포함 (현재 달 미포함) — 혹은 전 달까지
- 해당 월에 피드가 없으면 해당 월은 결과에서 제외
- 동점 시 feedId ASC 기준으로 1위 결정

---

## 구현할 파일 목록

### 1. `repository/dto/FeedRankingWinnerDto.java` (native query projection)
```java
public interface FeedRankingWinnerDto {
    Integer getMonth();
    Long getFeedId();
    String getClubName();
    String getActivityContent();
    String getFeedType();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Long getTotalScore();
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        MONTH(f.created_at) AS month,
        f.id                AS feedId,
        c.name              AS clubName,
        f.activity_content  AS activityContent,
        f.feed_type         AS feedType,
        f.view_count        AS viewCount,
        COUNT(DISTINCT fl.id) AS likeCount,
        COUNT(DISTINCT fc.id) AS commentCount,
        f.view_count + COUNT(DISTINCT fl.id) + COUNT(DISTINCT fc.id) AS totalScore
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND YEAR(f.created_at) = :year
    GROUP BY MONTH(f.created_at), f.id, c.name, f.activity_content, f.feed_type, f.view_count
    HAVING totalScore = (
        SELECT MAX(sub.total)
        FROM (
            SELECT f2.view_count + COUNT(DISTINCT fl2.id) + COUNT(DISTINCT fc2.id) AS total
            FROM feed f2
            LEFT JOIN feed_like fl2 ON fl2.feed_id = f2.id
            LEFT JOIN feed_comment fc2 ON fc2.feed_id = f2.id AND fc2.deleted_at IS NULL
            WHERE f2.deleted_at IS NULL
              AND YEAR(f2.created_at) = :year
              AND MONTH(f2.created_at) = MONTH(f.created_at)
            GROUP BY f2.id
        ) sub
    )
    ORDER BY MONTH(f.created_at) ASC, f.id ASC
    """, nativeQuery = true)
List<FeedRankingWinnerDto> findYearlyWinners(@Param("year") int year);
```

> **Note**: HAVING 절 서브쿼리 대신 애플리케이션 레벨에서 월별 grouping 후 최고점 필터링하는 방법도 고려.
> 단순 구현: `findMonthlyRanking`을 1~12월 반복 호출 후 각 월 첫 번째만 추출.

### 3. `service/FeedRankingService.java` — 메서드 추가
```java
List<FeedRankingWinnerQuery> getYearlyWinners(int year);
```

### 4. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public List<FeedRankingWinnerQuery> getYearlyWinners(int year) {
    return feedRepository.findYearlyWinners(year)
            .stream()
            .map(FeedRankingWinnerQuery::from)
            .toList();
}
```

### 5. `service/dto/query/FeedRankingWinnerQuery.java`
```java
@Builder
public record FeedRankingWinnerQuery(
    int month,
    Long feedId,
    String clubName,
    String activityContent,
    String feedType,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    Long totalScore
) {
    public static FeedRankingWinnerQuery from(FeedRankingWinnerDto dto) { ... }
}
```

### 6. `controller/dto/response/AdminFeedRankingWinnersResponse.java`
```java
@Builder
public record AdminFeedRankingWinnersResponse(List<WinnerItem> winners) {

    public static AdminFeedRankingWinnersResponse from(List<FeedRankingWinnerQuery> queries) { ... }

    @Builder
    public record WinnerItem(
        int month,
        Long feedId,
        String clubName,
        String activityContent,
        String feedType,
        Long totalScore,
        Long viewCount,
        Long likeCount,
        Long commentCount
    ) { }
}
```

### 7. `api/AdminFeedRankingApi.java` — 메서드 추가
```java
@Operation(summary = "연도별 월 1위 피드 목록 조회 API")
@ApiResponse(responseCode = "200", ...)
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/winners")
AdminFeedRankingWinnersResponse getRankingWinners(@RequestParam("year") int year);
```

### 8. `controller/AdminFeedRankingController.java` — 메서드 추가
```java
@Override
public AdminFeedRankingWinnersResponse getRankingWinners(int year) {
    return AdminFeedRankingWinnersResponse.from(feedRankingService.getYearlyWinners(year));
}
```

---

## 완료 기준
- [ ] GET `/server/admin/feed-rankings/winners?year=2025` 200 응답
- [ ] 각 월의 totalScore 최고 피드 1개씩 반환
- [ ] 피드 없는 달은 결과에서 제외
- [ ] month 오름차순 정렬
- [ ] ADMIN 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `AdminFeedRankingApiTest` (통합, 기존 클래스에 추가)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 연도별 1위 목록 조회 성공 | ADMIN 인증, year=2025, 3개월 데이터 존재 | 200 + winners 3개 |
| 월별 1위만 포함 | 3월에 피드 3개, 각 점수 다름 | 3월 winner는 최고 점수 피드 1개 |
| 피드 없는 달 제외 | 1월만 데이터, 2~12월 없음 | winners.size()=1 |
| month 오름차순 정렬 | 3월, 1월 데이터 존재 | winners[0].month=1, winners[1].month=3 |
| ADMIN 아닌 접근 | ROLE_CLUB 토큰 | 403 |
