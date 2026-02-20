# Task 07: 총동연 피드 랭킹 API

## Overview
총동연(Admin) 권한으로 접근하는 월별 피드 랭킹 전체 조회, 지난 피드 랭킹 1위 전체 조회 API를 구현한다.

## Dependencies
- Task 05 (Ranking Service)

## Files to Create/Modify

### 1. 생성: `domain/feed/api/AdminFeedRankingApi.java`

```java
@Tag(name = "Feed Ranking - Admin", description = "총동연 피드 랭킹 API")
@RequestMapping("/server/admin")
public interface AdminFeedRankingApi {

    @Operation(summary = "월별 피드 랭킹 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "월별 피드 랭킹 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/feed-rankings/monthly")
    List<MonthlyFeedRankingResponse> getMonthlyFeedRanking(
        @RequestParam("year") int year,
        @RequestParam("month") int month
    );

    @Operation(summary = "지난 피드 랭킹 1위 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "지난 피드 랭킹 1위 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/feed-rankings/winners")
    List<PastFeedRankingWinnerResponse> getPastFeedRankingWinners(
        @RequestParam("year") int year
    );
}
```

### 2. 생성: `domain/feed/controller/AdminFeedRankingController.java`

### 3. 생성: Response DTO

- `domain/feed/controller/dto/response/MonthlyFeedRankingResponse.java`

```java
@Builder
public record MonthlyFeedRankingResponse(
    @Schema(description = "순위", example = "1")
    int rank,
    @Schema(description = "동아리명", example = "카우")
    String clubName,
    @Schema(description = "조회수", example = "150")
    long viewCount,
    @Schema(description = "댓글수", example = "30")
    long commentCount,
    @Schema(description = "좋아요수", example = "45")
    long likeCount,
    @Schema(description = "총점", example = "240")
    long totalScore
) {
    public static MonthlyFeedRankingResponse from(MonthlyFeedRankingQuery query) {
        return MonthlyFeedRankingResponse.builder()
                .rank(query.rank())
                .clubName(query.clubName())
                .viewCount(query.viewCount())
                .commentCount(query.commentCount())
                .likeCount(query.likeCount())
                .totalScore(query.totalScore())
                .build();
    }
}
```

- `domain/feed/controller/dto/response/PastFeedRankingWinnerResponse.java`

```java
@Builder
public record PastFeedRankingWinnerResponse(
    @Schema(description = "날짜 (년-월)", example = "2026-01")
    String date,
    @Schema(description = "동아리명", example = "카우")
    String clubName,
    @Schema(description = "조회수", example = "200")
    long viewCount,
    @Schema(description = "댓글수", example = "50")
    long commentCount,
    @Schema(description = "좋아요수", example = "80")
    long likeCount,
    @Schema(description = "총점", example = "410")
    long totalScore
) {
    public static PastFeedRankingWinnerResponse from(PastFeedRankingWinnerQuery query) { ... }
}
```

## URL Routing

| Method | URL | Query Params | Description | Auth |
|--------|-----|--------------|-------------|------|
| GET | `/server/admin/feed-rankings/monthly` | year, month | 월별 랭킹 | ADMIN |
| GET | `/server/admin/feed-rankings/winners` | year | 지난 1위 목록 | ADMIN |

## Acceptance Criteria
- [ ] 월별 랭킹: year, month 파라미터로 전체 동아리 랭킹 반환 (순위, 동아리명, 조회수, 댓글수, 좋아요수, 총점)
- [ ] 지난 1위: year 파라미터로 해당 연도 각 월의 1위 반환 (날짜, 동아리명, 조회수, 댓글수, 좋아요수, 총점)
- [ ] ADMIN 권한 없는 사용자 접근 시 403 응답
- [ ] Swagger UI에 API 노출

## Notes
- `/server/admin/**` 경로는 SecurityConfig에서 이미 ROLE_ADMIN 으로 보호됨
- 추가 SecurityConfig 변경 불필요
