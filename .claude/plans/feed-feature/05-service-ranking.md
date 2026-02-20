# Task 05: 피드 랭킹 Service 레이어

## Overview
총동연(Admin)용 월별 피드 랭킹 조회, 지난 1위 조회와 동아리 회장(Club)용 피드 현황 조회를 위한 Service 레이어를 구현한다.

## Dependencies
- Task 03 (Repository)
- Task 04 (Like/Comment Service)

## Files to Create/Modify

### 1. 생성: `domain/feed/service/FeedRankingService.java` (인터페이스)

```java
public interface FeedRankingService {

    // 월별 전체 피드 랭킹 조회 (총동연)
    List<MonthlyFeedRankingQuery> getMonthlyRanking(int year, int month);

    // 지난 피드 랭킹 1위 전체 조회 (총동연)
    List<PastFeedRankingWinnerQuery> getPastRankingWinners(int year);

    // 동아리별 전체 피드의 개별 랭킹 (동아리 회장)
    List<ClubFeedRankingQuery> getClubFeedRanking(Long clubId, int year);

    // 이달의 피드 점수 현황 (동아리 회장)
    ClubMonthlyBestFeedQuery getClubMonthlyBestFeed(Long clubId);
}
```

### 2. 생성: `domain/feed/service/GeneralFeedRankingService.java`

주요 로직:
- 월별 랭킹: FeedRepository의 네이티브 쿼리로 club별 집계 후 순위 매기기
- 지난 1위: 해당 연도의 각 월별 랭킹 1위를 조회 (현재 월 제외)
- 동아리 피드 랭킹: 특정 동아리의 개별 피드별 점수를 계산하여 전체 랭킹 내 순위 산출
- 이달의 피드: 이번 달 생성된 피드 중 가장 높은 순위의 피드 정보 반환

**총점 계산 공식:**
```
totalScore = viewCount * 1 + commentCount * 2 + likeCount * 3
```

### 3. Query DTO 생성

- `domain/feed/service/dto/query/MonthlyFeedRankingQuery.java`

```java
public record MonthlyFeedRankingQuery(
    int rank,
    String clubName,
    long viewCount,
    long commentCount,
    long likeCount,
    long totalScore
) {}
```

- `domain/feed/service/dto/query/PastFeedRankingWinnerQuery.java`

```java
public record PastFeedRankingWinnerQuery(
    String date,       // "2026-01" 형식
    String clubName,
    long viewCount,
    long commentCount,
    long likeCount,
    long totalScore
) {}
```

- `domain/feed/service/dto/query/ClubFeedRankingQuery.java`

```java
public record ClubFeedRankingQuery(
    Long feedId,
    int rank,
    long viewCount,
    long commentCount,
    long likeCount,
    long totalScore
) {}
```

- `domain/feed/service/dto/query/ClubMonthlyBestFeedQuery.java`

```java
public record ClubMonthlyBestFeedQuery(
    int rank,
    long viewCount,
    long commentCount,
    long likeCount,
    long totalScore
) {}
```

## Acceptance Criteria
- [ ] 월별 랭킹: 년/월 파라미터로 전체 동아리의 피드 랭킹 반환
- [ ] 지난 1위: 해당 연도의 각 월별 1위 동아리 목록 반환
- [ ] 동아리 피드 랭킹: 해당 동아리 피드의 전체 랭킹 내 순위 반환 (최신순 정렬)
- [ ] 이달의 피드: 이번 달 피드 중 최고 순위 피드 정보 반환
- [ ] 총점 계산 공식이 일관되게 적용

## Notes
- 총점 가중치(1, 2, 3)는 상수로 관리하여 추후 변경 용이하게 할 것
- 랭킹 데이터가 없는 경우 빈 리스트 또는 기본값 반환
- 지난 1위 조회 시 현재 월은 제외 (아직 진행 중이므로)
- 성능 최적화: 네이티브 쿼리로 DB 레벨에서 집계 처리
