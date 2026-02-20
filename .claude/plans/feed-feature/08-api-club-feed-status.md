# Task 08: 동아리 회장 피드 현황 API

## Overview
동아리 회장(Club) 권한으로 접근하는 전체 피드 개별 랭킹 조회, 이달의 피드 점수 현황 조회 API를 구현한다.

## Dependencies
- Task 05 (Ranking Service)

## Files to Create/Modify

### 1. 생성: `domain/feed/api/ClubFeedRankingApi.java`

```java
@Tag(name = "Feed Ranking - Club", description = "동아리 회장 피드 현황 API")
@RequestMapping("/server/central")
public interface ClubFeedRankingApi {

    @Operation(summary = "전체 피드의 개별 랭킹 조회 API")
    @ApiResponse(responseCode = "200", description = "피드 개별 랭킹 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/feed-rankings")
    List<ClubFeedRankingResponse> getMyFeedRanking(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(value = "year", required = false) Integer year
    );

    @Operation(summary = "이달의 피드 점수 현황 조회 API")
    @ApiResponse(responseCode = "200", description = "이달의 피드 현황 조회 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/feed-rankings/monthly-best")
    ClubMonthlyBestFeedResponse getMyMonthlyBestFeed(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
```

### 2. 생성: `domain/feed/controller/ClubFeedRankingController.java`

### 3. 생성: Response DTO

- `domain/feed/controller/dto/response/ClubFeedRankingResponse.java`

```java
@Builder
public record ClubFeedRankingResponse(
    @Schema(description = "피드 ID", example = "1")
    Long feedId,
    @Schema(description = "순위", example = "3")
    int rank,
    @Schema(description = "조회수", example = "50")
    long viewCount,
    @Schema(description = "댓글수", example = "10")
    long commentCount,
    @Schema(description = "좋아요수", example = "15")
    long likeCount,
    @Schema(description = "총점", example = "95")
    long totalScore
) {
    public static ClubFeedRankingResponse from(ClubFeedRankingQuery query) { ... }
}
```

- `domain/feed/controller/dto/response/ClubMonthlyBestFeedResponse.java`

```java
@Builder
public record ClubMonthlyBestFeedResponse(
    @Schema(description = "순위", example = "1")
    int rank,
    @Schema(description = "조회수", example = "100")
    long viewCount,
    @Schema(description = "댓글수", example = "20")
    long commentCount,
    @Schema(description = "좋아요수", example = "30")
    long likeCount,
    @Schema(description = "총점", example = "190")
    long totalScore
) {
    public static ClubMonthlyBestFeedResponse from(ClubMonthlyBestFeedQuery query) { ... }
}
```

## URL Routing

| Method | URL | Query Params | Description | Auth |
|--------|-----|--------------|-------------|------|
| GET | `/server/central/my/feed-rankings` | year (optional) | 내 피드 개별 랭킹 | CLUB |
| GET | `/server/central/my/feed-rankings/monthly-best` | - | 이달의 최고 피드 | CLUB |

## Acceptance Criteria
- [ ] 피드 개별 랭킹: 해당 동아리 피드의 전체 순위, 연도 필터링, 최신순 정렬
- [ ] 이달의 피드: 이번 달 피드 중 가장 높은 순위의 피드 정보 반환
- [ ] year 파라미터 미전달 시 현재 연도 기본값 적용
- [ ] CLUB 권한 없는 사용자 접근 시 403 응답
- [ ] 이달에 피드가 없는 경우 적절한 기본값 반환

## Notes
- `/server/central/**` 경로는 SecurityConfig에서 이미 ROLE_CLUB으로 보호됨
- ClubService.getByUserId()로 현재 로그인한 사용자의 Club 정보 조회
- 이달의 피드 현황은 "이달 생성 피드 중 가장 높은 순위" -> 전체 피드 랭킹에서의 순위
