# API 04: 동아리 회장 피드 개별 랭킹 조회

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/central/my/feed-rankings?year=` | CLUB | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 |

### Response Body
```json
{
  "rankings": [
    {
      "month": 3,
      "rank": 2,
      "feedId": 10,
      "activityContent": "피드 내용",
      "feedType": "IMAGE",
      "totalScore": 120,
      "viewCount": 80,
      "likeCount": 25,
      "commentCount": 15
    }
  ]
}
```

### 비즈니스 규칙
- 로그인한 동아리 회장의 Club 소속 피드만 대상
- 해당 연도 각 월별로 내 피드의 랭킹(전체 피드 중 순위) 반환
- 월별로 그룹핑하여 반환 (피드가 없는 달은 제외)
- 같은 달에 내 피드가 여러 개면 모두 반환 (각각의 순위 포함)

---

## 구현할 파일 목록

### 1. `repository/dto/ClubFeedRankingDto.java` (native query projection)
```java
public interface ClubFeedRankingDto {
    Integer getMonth();
    Long getFeedId();
    String getActivityContent();
    String getFeedType();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Long getTotalScore();
    Long getRankInMonth(); // 해당 월 전체 중 순위
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        MONTH(f.created_at) AS month,
        f.id                AS feedId,
        f.activity_content  AS activityContent,
        f.feed_type         AS feedType,
        f.view_count        AS viewCount,
        COUNT(DISTINCT fl.id) AS likeCount,
        COUNT(DISTINCT fc.id) AS commentCount,
        f.view_count + COUNT(DISTINCT fl.id) + COUNT(DISTINCT fc.id) AS totalScore,
        RANK() OVER (
            PARTITION BY MONTH(f.created_at)
            ORDER BY (f.view_count + COUNT(DISTINCT fl.id) + COUNT(DISTINCT fc.id)) DESC, f.id ASC
        ) AS rankInMonth
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND YEAR(f.created_at) = :year
      AND f.club_id = (SELECT club_id FROM users WHERE id = :userId)
    GROUP BY MONTH(f.created_at), f.id, f.activity_content, f.feed_type, f.view_count, f.created_at
    ORDER BY MONTH(f.created_at) ASC, totalScore DESC
    """, nativeQuery = true)
List<ClubFeedRankingDto> findClubFeedRanking(
        @Param("userId") Long userId,
        @Param("year") int year
);
```

> **Note**: Window function `RANK() OVER` 대신 서브쿼리로 랭킹 계산도 가능.
> 또는 애플리케이션 레벨에서 전체 월별 랭킹 조회 후 club 필터링하는 방법.

### 3. `service/FeedRankingService.java` — 메서드 추가
```java
List<ClubFeedRankingQuery> getClubFeedRanking(Long userId, int year);
```

### 4. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public List<ClubFeedRankingQuery> getClubFeedRanking(Long userId, int year) {
    return feedRepository.findClubFeedRanking(userId, year)
            .stream()
            .map(ClubFeedRankingQuery::from)
            .toList();
}
```

### 5. `service/dto/query/ClubFeedRankingQuery.java`
```java
@Builder
public record ClubFeedRankingQuery(
    int month,
    Long feedId,
    String activityContent,
    String feedType,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    Long totalScore,
    long rankInMonth
) {
    public static ClubFeedRankingQuery from(ClubFeedRankingDto dto) { ... }
}
```

### 6. `controller/dto/response/ClubFeedRankingResponse.java`
```java
@Builder
public record ClubFeedRankingResponse(List<RankingItem> rankings) {

    public static ClubFeedRankingResponse from(List<ClubFeedRankingQuery> queries) { ... }

    @Builder
    public record RankingItem(
        int month,
        long rank,
        Long feedId,
        String activityContent,
        String feedType,
        Long totalScore,
        Long viewCount,
        Long likeCount,
        Long commentCount
    ) { }
}
```

### 7. `api/ClubFeedApi.java` — 메서드 추가
```java
@Operation(summary = "동아리 회장 피드 개별 랭킹 조회 API")
@ApiResponse(responseCode = "200", ...)
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/my/feed-rankings")
ClubFeedRankingResponse getMyFeedRanking(
    @AuthenticationPrincipal PrincipalDetails principalDetails,
    @RequestParam("year") int year
);
```

### 8. `controller/ClubFeedController.java` — 메서드 추가
```java
@Override
public ClubFeedRankingResponse getMyFeedRanking(PrincipalDetails principalDetails, int year) {
    return ClubFeedRankingResponse.from(
            feedRankingService.getClubFeedRanking(principalDetails.getId(), year));
}
```

> **Note**: ClubFeedController에 FeedRankingService 의존성 추가 필요

---

## 완료 기준
- [ ] GET `/server/central/my/feed-rankings?year=2025` 200 응답
- [ ] 로그인한 동아리 클럽 피드만 반환
- [ ] 각 피드의 해당 월 전체 랭킹 정확히 계산
- [ ] CLUB 이외 접근 시 403 응답
- [ ] 피드 없는 달은 결과에서 제외
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `ClubFeedRankingApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 개별 랭킹 조회 성공 | CLUB 인증, year=2025, 본인 피드 존재 | 200 + rankings 배열 |
| 본인 club 피드만 반환 | 동아리A·B 피드 혼재 | 동아리A 회장은 동아리A 피드만 포함 |
| 랭킹 정확성 | 전체 3개 피드, 내 피드가 2위 | rankInMonth=2 |
| 피드 없는 달 제외 | 특정 달 내 피드 없음 | 해당 달 미포함 |
| CLUB 아닌 접근 | 일반 USER 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |
