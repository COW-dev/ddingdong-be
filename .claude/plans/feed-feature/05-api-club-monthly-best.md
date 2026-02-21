# API 05: 동아리 회장 이달의 피드 현황 조회

## API 명세

| Method | URL | Auth | 상태코드 |
|--------|-----|------|---------|
| GET | `/server/central/my/feed-rankings/monthly-best` | CLUB | 200 |

### Response Body
```json
{
  "year": 2025,
  "month": 3,
  "myBestFeed": {
    "feedId": 10,
    "activityContent": "피드 내용",
    "feedType": "IMAGE",
    "totalScore": 150,
    "viewCount": 100,
    "likeCount": 30,
    "commentCount": 20,
    "rankInMonth": 2
  },
  "topFeed": {
    "feedId": 5,
    "clubName": "1위 동아리",
    "activityContent": "1위 피드 내용",
    "feedType": "IMAGE",
    "totalScore": 300,
    "viewCount": 200,
    "likeCount": 60,
    "commentCount": 40
  }
}
```

### 비즈니스 규칙
- 현재 월(서버 시간 기준) 데이터만 조회
- `myBestFeed`: 내 동아리 피드 중 이달 최고 점수 피드 1개 + 해당 피드의 전체 순위
- `topFeed`: 이달 전체 1위 피드 정보
- 내 피드가 없으면 `myBestFeed: null`
- 전체 피드가 없으면 `topFeed: null`

---

## 구현할 파일 목록

### 1. `service/FeedRankingService.java` — 메서드 추가
```java
MonthlyBestFeedQuery getMonthlyBestFeed(Long userId);
```

### 2. `service/GeneralFeedRankingService.java` — 메서드 추가
```java
@Override
public MonthlyBestFeedQuery getMonthlyBestFeed(Long userId) {
    LocalDate now = LocalDate.now();
    int year = now.getYear();
    int month = now.getMonthValue();

    // 이달 전체 랭킹 (기존 findMonthlyRanking 재사용)
    List<MonthlyFeedRankingDto> allRankings = feedRepository.findMonthlyRanking(year, month);

    // topFeed: 전체 1위
    MonthlyFeedRankingDto topDto = allRankings.isEmpty() ? null : allRankings.get(0);

    // myBestFeed: 내 club_id 소속 피드 중 최고 점수 + 해당 rankInMonth
    // null-safe: 클럽 미소속 사용자 방어
    Long clubId = userRepository.findById(userId)
            .map(User::getClub)
            .map(Club::getId)
            .orElseThrow(() -> new UserException.ClubNotFoundException());

    MonthlyFeedRankingDto myBestDto = null;
    long myRank = 0;
    long currentRank = 1;
    for (int i = 0; i < allRankings.size(); i++) {
        // 동점 처리: totalScore가 바뀔 때만 순위 증가 (SQL RANK() 방식과 동일)
        if (i > 0 && !allRankings.get(i).getTotalScore().equals(allRankings.get(i - 1).getTotalScore())) {
            currentRank = i + 1;
        }
        MonthlyFeedRankingDto dto = allRankings.get(i);
        if (dto.getClubId().equals(clubId) && myBestDto == null) {
            // 이미 totalScore DESC 정렬이므로 첫 번째 매칭이 최고 점수
            myBestDto = dto;
            myRank = currentRank;
            break;
        }
    }

    return MonthlyBestFeedQuery.of(year, month, myBestDto, myRank, topDto);
}
```

> **Note**: `MonthlyFeedRankingDto`에 `clubId` 필드 추가 필요 (02 plan의 projection 수정)

### 3. `service/dto/query/MonthlyBestFeedQuery.java`
```java
@Builder
public record MonthlyBestFeedQuery(
    int year,
    int month,
    MyBestFeedInfo myBestFeed,    // nullable
    TopFeedInfo topFeed           // nullable
) {
    public static MonthlyBestFeedQuery of(...) { ... }

    @Builder
    public record MyBestFeedInfo(
        Long feedId,
        String activityContent,
        String feedType,
        Long totalScore,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        long rankInMonth
    ) { }

    @Builder
    public record TopFeedInfo(
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

### 4. `controller/dto/response/ClubMonthlyBestFeedResponse.java`
```java
@Builder
public record ClubMonthlyBestFeedResponse(
    int year,
    int month,
    MyBestFeedResponse myBestFeed,
    TopFeedResponse topFeed
) {
    public static ClubMonthlyBestFeedResponse from(MonthlyBestFeedQuery query) { ... }

    @Builder
    public record MyBestFeedResponse(
        Long feedId, String activityContent, String feedType,
        Long totalScore, Long viewCount, Long likeCount, Long commentCount, long rankInMonth
    ) { }

    @Builder
    public record TopFeedResponse(
        Long feedId, String clubName, String activityContent, String feedType,
        Long totalScore, Long viewCount, Long likeCount, Long commentCount
    ) { }
}
```

### 5. `api/ClubFeedApi.java` — 메서드 추가
```java
@Operation(summary = "동아리 회장 이달의 피드 현황 조회 API")
@ApiResponse(responseCode = "200", ...)
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "AccessToken")
@GetMapping("/my/feed-rankings/monthly-best")
ClubMonthlyBestFeedResponse getMyMonthlyBestFeed(
    @AuthenticationPrincipal PrincipalDetails principalDetails
);
```

### 6. `controller/ClubFeedController.java` — 메서드 추가
```java
@Override
public ClubMonthlyBestFeedResponse getMyMonthlyBestFeed(PrincipalDetails principalDetails) {
    return ClubMonthlyBestFeedResponse.from(
            feedRankingService.getMonthlyBestFeed(principalDetails.getId()));
}
```

---

## 구현 시 주의사항
- `MonthlyFeedRankingDto`에 `clubId` 필드 추가 필요 → 02 plan의 native query도 수정
- User→Club 조회를 위해 UserService 또는 ClubService 의존성 필요
- 내 피드가 전체 1위인 경우 `myBestFeed`와 `topFeed`가 같은 피드를 가리킬 수 있음 (정상)

---

## 완료 기준
- [ ] GET `/server/central/my/feed-rankings/monthly-best` 200 응답
- [ ] myBestFeed에 이달 내 최고 점수 피드 + 전체 순위 포함
- [ ] topFeed에 이달 전체 1위 피드 포함
- [ ] 피드 없을 때 해당 필드 null 반환
- [ ] CLUB 이외 접근 시 403 응답
- [ ] Swagger UI 노출

---

## 테스트 케이스

### `ClubMonthlyBestApiTest` (통합)

| 테스트 | 조건 | 기대 결과 |
|--------|------|---------|
| 이달 현황 조회 성공 | CLUB 인증, 이달 본인 피드 + 타 동아리 피드 존재 | 200 + myBestFeed, topFeed 모두 포함 |
| 내 피드 없음 | 이달 내 동아리 피드 없음 | `myBestFeed: null`, topFeed는 정상 반환 |
| 전체 피드 없음 | 이달 피드 전혀 없음 | `myBestFeed: null`, `topFeed: null` |
| 내 피드가 1위 | 내 피드 totalScore 최고 | myBestFeed.rankInMonth=1, topFeed.feedId == myBestFeed.feedId |
| rankInMonth 정확성 | 전체 5개 피드, 내 피드 3위 | myBestFeed.rankInMonth=3 |
| CLUB 아닌 접근 | 일반 USER 토큰 | 403 |
| 미인증 접근 | Authorization 없음 | 401 |
