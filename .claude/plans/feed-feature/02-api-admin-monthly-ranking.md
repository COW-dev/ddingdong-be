# API 02: 총동연 월별 피드 랭킹 조회 (API 8)

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/admin/feeds/ranking?year=&month=` | ROLE_ADMIN | 200 |

### Query Parameters
| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| year | int | Y | 조회 연도 (예: 2025) |
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
    "viewCount": 100,
    "likeCount": 10,
    "commentCount": 8
  }
]
```

### 랭킹 점수 계산
```
score = viewCount × 1 + likeCount × 3 + commentCount × 5
```
정렬: score DESC, feedId ASC (동점 시)
대상: 해당 연월에 createdAt이 속하는 피드

---

## 구현할 파일 목록

### 1. `repository/dto/MonthlyFeedRankingDto.java` (native query projection)
```java
public interface MonthlyFeedRankingDto {
    Long getFeedId();
    String getThumbnailUrl();
    String getClubName();
    Long getViewCount();
    Long getLikeCount();
    Long getCommentCount();
    Long getScore();
}
```

> **Note**: 기존 plan의 activityContent, feedType, createdAt, clubId 필드 제거.
> thumbnailUrl 추가 (피드 대표 이미지 URL).

### 2. `repository/FeedRepository.java` — 쿼리 추가
```java
@Query(value = """
    SELECT
        f.id              AS feedId,
        f.thumbnail_url   AS thumbnailUrl,
        c.name            AS clubName,
        f.view_count      AS viewCount,
        COUNT(DISTINCT fl.id) AS likeCount,
        COUNT(DISTINCT fc.id) AS commentCount,
        (f.view_count * 1 + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) AS score
    FROM feed f
    JOIN club c ON f.club_id = c.id
    LEFT JOIN feed_like fl ON fl.feed_id = f.id
    LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
    WHERE f.deleted_at IS NULL
      AND YEAR(f.created_at) = :year
      AND MONTH(f.created_at) = :month
    GROUP BY f.id, f.thumbnail_url, c.name, f.view_count
    ORDER BY score DESC, f.id ASC
    """, nativeQuery = true)
List<MonthlyFeedRankingDto> findMonthlyRanking(
        @Param("year") int year,
        @Param("month") int month
);
```

> **랭킹 공식**: `score = viewCount×1 + likeCount×3 + commentCount×5`

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
    String thumbnailUrl,
    String clubName,
    Long viewCount,
    Long likeCount,
    Long commentCount,
    Long score
) {
    public static MonthlyFeedRankingQuery from(MonthlyFeedRankingDto dto) {
        return MonthlyFeedRankingQuery.builder()
                .feedId(dto.getFeedId())
                .thumbnailUrl(dto.getThumbnailUrl())
                .clubName(dto.getClubName())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .score(dto.getScore())
                .build();
    }
}
```

### 6. `controller/dto/response/AdminMonthlyFeedRankingResponse.java`
```java
@Builder
public record AdminMonthlyFeedRankingResponse(
    int rank,
    Long feedId,
    String thumbnailUrl,
    String clubName,
    Long score,
    Long viewCount,
    Long likeCount,
    Long commentCount
) {
    public static List<AdminMonthlyFeedRankingResponse> from(
            List<MonthlyFeedRankingQuery> queries) {
        List<AdminMonthlyFeedRankingResponse> result = new ArrayList<>();
        int rank = 1;
        for (int i = 0; i < queries.size(); i++) {
            if (i > 0 && !queries.get(i).score().equals(queries.get(i - 1).score())) {
                rank = i + 1;
            }
            MonthlyFeedRankingQuery q = queries.get(i);
            result.add(AdminMonthlyFeedRankingResponse.builder()
                    .rank(rank)
                    .feedId(q.feedId())
                    .thumbnailUrl(q.thumbnailUrl())
                    .clubName(q.clubName())
                    .score(q.score())
                    .viewCount(q.viewCount())
                    .likeCount(q.likeCount())
                    .commentCount(q.commentCount())
                    .build());
        }
        return result;
    }
}
```

### 7. `api/AdminFeedApi.java` (또는 기존 Admin API에 추가)
```java
@Tag(name = "Feed - Admin", description = "총동연 피드 API")
@RequestMapping("/server/admin/feeds")
public interface AdminFeedApi {

    @Operation(summary = "월별 피드 랭킹 조회 API")
    @ApiResponse(responseCode = "200", description = "월별 랭킹 조회 성공",
        content = @Content(array = @ArraySchema(
            schema = @Schema(implementation = AdminMonthlyFeedRankingResponse.class))))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/ranking")
    List<AdminMonthlyFeedRankingResponse> getMonthlyRanking(
        @RequestParam("year") @Min(2000) @Max(2100) int year,
        @RequestParam("month") @Min(1) @Max(12) int month
    );
}
```

### 8. `controller/AdminFeedController.java`
```java
@RestController
@RequiredArgsConstructor
public class AdminFeedController implements AdminFeedApi {

    private final FeedRankingService feedRankingService;

    @Override
    public List<AdminMonthlyFeedRankingResponse> getMonthlyRanking(int year, int month) {
        return AdminMonthlyFeedRankingResponse.from(
                feedRankingService.getMonthlyRanking(year, month));
    }
}
```

---

## 완료 기준
- [ ] GET `/server/admin/feeds/ranking?year=2025&month=3` 200 응답
- [ ] score = viewCount×1 + likeCount×3 + commentCount×5 정확히 계산
- [ ] score DESC 정렬, 동점 시 feedId ASC
- [ ] 해당 월 피드가 없으면 빈 배열 반환
- [ ] ADMIN 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `AdminFeedRankingApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 월별 랭킹 조회 성공 | ADMIN 인증, year=2025, month=3 | 200 + 배열 |
| score 계산 검증 | viewCount=10, likeCount=5, commentCount=3 | score=10+15+15=40 |
| score DESC 정렬 | 여러 피드 존재 | 첫 번째 아이템이 최고 점수 |
| 해당 월 피드 없음 | 빈 달 조회 | 200 + `[]` |
| ADMIN 아닌 접근 | ROLE_CLUB 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |

### `GeneralFeedRankingServiceTest` (단위)

| 테스트 | 검증 내용 |
|--------|---------|
| getMonthlyRanking | FeedRepository.findMonthlyRanking 호출 확인 |
| DTO → Query 변환 | MonthlyFeedRankingDto → MonthlyFeedRankingQuery 필드 매핑 정확성 |
