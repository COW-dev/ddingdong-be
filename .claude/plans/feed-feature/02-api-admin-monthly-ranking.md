# API 02: 총동연 월별 피드 랭킹 조회

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/admin/feed-rankings/monthly?year=&month=` | ADMIN | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 (예: 2025) |
| month | int | Y | 조회 월 (1~12) |

### Response Body
```json
{
  "rankings": [
    {
      "rank": 1,
      "clubName": "동아리명",
      "feedId": 10,
      "activityContent": "피드 내용",
      "feedType": "IMAGE",
      "totalScore": 150,
      "viewCount": 100,
      "likeCount": 30,
      "commentCount": 20,
      "createdAt": "2025-03-15T10:00:00"
    }
  ]
}
```

### 랭킹 점수 계산
`totalScore = viewCount + likeCount + commentCount`

정렬: totalScore DESC, feedId ASC (동점 시)
대상: 해당 연월에 createdAt이 속하는 피드

---

## 구현할 파일 목록

### 1. `repository/dto/MonthlyFeedRankingDto.java` (native query projection)
```java
public interface MonthlyFeedRankingDto {
    Long getFeedId();
    Long getClubId();      // 05 plan의 myBestFeed 필터링에 필요
    String getClubName();
    String getActivityContent();
    String getFeedType();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Long getTotalScore();
    LocalDateTime getCreatedAt();
}
```

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        f.id              AS feedId,
        c.id              AS clubId,
        c.name            AS clubName,
        f.activity_content AS activityContent,
        f.feed_type       AS feedType,
        f.view_count      AS viewCount,
        COUNT(DISTINCT fl.id) AS likeCount,
        COUNT(DISTINCT fc.id) AS commentCount,
        f.view_count + COUNT(DISTINCT fl.id) + COUNT(DISTINCT fc.id) AS totalScore,
        f.created_at      AS createdAt
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND YEAR(f.created_at) = :year
      AND MONTH(f.created_at) = :month
    GROUP BY f.id, c.id, c.name, f.activity_content, f.feed_type, f.view_count, f.created_at
    ORDER BY totalScore DESC, f.id ASC
    """, nativeQuery = true)
List<MonthlyFeedRankingDto> findMonthlyRanking(
        @Param("year") int year,
        @Param("month") int month
);
```

### 3. `service/FeedRankingService.java` (인터페이스)
```java
public interface FeedRankingService {
    List<MonthlyFeedRankingQuery> getMonthlyRanking(int year, int month);
}
```

### 4. `service/GeneralFeedRankingService.java`
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralFeedRankingService implements FeedRankingService {

    private final FeedRepository feedRepository;

    @Override
    public List<MonthlyFeedRankingQuery> getMonthlyRanking(int year, int month) {
        return feedRepository.findMonthlyRanking(year, month)
                .stream()
                .map(MonthlyFeedRankingQuery::from)
                .toList();
    }
}
```

### 5. `service/dto/query/MonthlyFeedRankingQuery.java`
```java
@Builder
public record MonthlyFeedRankingQuery(
    Long feedId,
    Long clubId,       // 05 plan의 myBestFeed 필터링에 필요
    String clubName,
    String activityContent,
    String feedType,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    Long totalScore,
    LocalDateTime createdAt
) {
    public static MonthlyFeedRankingQuery from(MonthlyFeedRankingDto dto) {
        return MonthlyFeedRankingQuery.builder()
                .feedId(dto.getFeedId())
                .clubId(dto.getClubId())
                .clubName(dto.getClubName())
                .activityContent(dto.getActivityContent())
                .feedType(dto.getFeedType())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .totalScore(dto.getTotalScore())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
```

### 6. `controller/dto/response/AdminMonthlyFeedRankingResponse.java`
```java
@Builder
public record AdminMonthlyFeedRankingResponse(List<RankingItem> rankings) {

    public static AdminMonthlyFeedRankingResponse from(List<MonthlyFeedRankingQuery> queries) {
        List<RankingItem> items = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < queries.size(); i++) {
            // 동점 시 같은 순위 부여 (SQL RANK() 방식)
            if (i > 0 && !queries.get(i).totalScore().equals(queries.get(i - 1).totalScore())) {
                rank = i + 1;
            }
            items.add(RankingItem.of(rank, queries.get(i)));
        }
        return new AdminMonthlyFeedRankingResponse(items);
    }

    @Builder
    public record RankingItem(
        int rank,
        Long feedId,
        String clubName,
        String activityContent,
        String feedType,
        Long totalScore,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        LocalDateTime createdAt
    ) {
        public static RankingItem of(int rank, MonthlyFeedRankingQuery query) { ... }
    }
}
```

### 7. `api/AdminFeedRankingApi.java`
```java
@Tag(name = "Feed Ranking - Admin", description = "총동연 피드 랭킹 API")
@RequestMapping("/server/admin/feed-rankings")
public interface AdminFeedRankingApi {

    @Operation(summary = "월별 피드 랭킹 조회 API")
    @ApiResponse(responseCode = "200", ...)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/monthly")
    AdminMonthlyFeedRankingResponse getMonthlyRanking(
        @RequestParam("year") @Min(2000) @Max(2100) int year,
        @RequestParam("month") @Min(1) @Max(12) int month
    );
}
```

### 8. `controller/AdminFeedRankingController.java`
```java
@RestController
@RequiredArgsConstructor
public class AdminFeedRankingController implements AdminFeedRankingApi {

    private final FeedRankingService feedRankingService;

    @Override
    public AdminMonthlyFeedRankingResponse getMonthlyRanking(int year, int month) {
        return AdminMonthlyFeedRankingResponse.from(
                feedRankingService.getMonthlyRanking(year, month));
    }
}
```

---

## 완료 기준
- [ ] GET `/server/admin/feed-rankings/monthly?year=2025&month=3` 200 응답
- [ ] totalScore = viewCount + likeCount + commentCount 정확히 계산
- [ ] totalScore DESC 정렬, 동점 시 feedId ASC
- [ ] 해당 월 피드가 없으면 빈 배열 반환
- [ ] ADMIN 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `AdminFeedRankingApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 월별 랭킹 조회 성공 | ADMIN 인증, year=2025, month=3 | 200 + rankings 배열 |
| totalScore 계산 검증 | viewCount=10, likeCount=5, commentCount=3 | totalScore=18 |
| totalScore DESC 정렬 | 여러 피드 존재 | 첫 번째 아이템이 최고 점수 |
| 해당 월 피드 없음 | 빈 달 조회 | 200 + `rankings: []` |
| ADMIN 아닌 접근 | ROLE_CLUB 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |

### `GeneralFeedRankingServiceTest` (단위)

| 테스트 | 검증 내용 |
|--------|---------|
| getMonthlyRanking | FeedRepository.findMonthlyRanking 호출 확인 |
| DTO → Query 변환 | MonthlyFeedRankingDto → MonthlyFeedRankingQuery 필드 매핑 정확성 |
