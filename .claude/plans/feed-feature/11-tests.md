# Task 11: 테스트 코드 작성

## Overview
신규/수정된 Service, Repository 레이어에 대한 테스트 코드를 작성한다.

## Dependencies
- Task 04, 05, 09 (Service 구현 완료 후)

## Test Strategy
- 기존 패턴: `@SpringBootTest` + `TestContainerSupport` + `FixtureMonkey` + `MockitoBean(S3FileService)`
- Service 레이어 통합 테스트 중심
- Repository 네이티브 쿼리는 별도 Repository 테스트

## Files to Create

### 1. `src/test/java/ddingdong/ddingdongBE/domain/feed/service/GeneralFeedLikeServiceTest.java`

테스트 케이스:
- [ ] 좋아요 생성 성공
- [ ] 동일 피드에 중복 좋아요 시 예외 발생
- [ ] 좋아요 취소(삭제) 성공
- [ ] 좋아요 수 조회 정확성
- [ ] 존재하지 않는 피드에 좋아요 시 예외 발생

### 2. `src/test/java/ddingdong/ddingdongBE/domain/feed/service/GeneralFeedCommentServiceTest.java`

테스트 케이스:
- [ ] 댓글 작성 성공
- [ ] 댓글 삭제 성공 (soft delete)
- [ ] 댓글 조회 시 삭제된 댓글 제외 확인
- [ ] 피드별 댓글 목록 조회 (시간순 정렬)
- [ ] 존재하지 않는 댓글 삭제 시 예외 발생

### 3. `src/test/java/ddingdong/ddingdongBE/domain/feed/service/GeneralFeedRankingServiceTest.java`

테스트 케이스:
- [ ] 월별 랭킹 조회 - 총점 기준 정렬 정확성
- [ ] 월별 랭킹 조회 - 데이터 없는 월 빈 리스트 반환
- [ ] 지난 1위 조회 - 각 월의 1위만 반환
- [ ] 동아리 피드 개별 랭킹 조회
- [ ] 이달의 피드 최고 순위 조회

### 4. `src/test/java/ddingdong/ddingdongBE/domain/feed/repository/FeedLikeRepositoryTest.java`

테스트 케이스:
- [ ] unique constraint 동작 확인
- [ ] countByFeedId 정확성
- [ ] deleteByFeedIdAndUserId 동작 확인

### 5. `src/test/java/ddingdong/ddingdongBE/domain/feed/repository/FeedCommentRepositoryTest.java`

테스트 케이스:
- [ ] findAllByFeedIdOrderByCreatedAtAsc 정렬 확인
- [ ] soft delete 후 조회에서 제외 확인

### 6. 수정: `src/test/java/ddingdong/ddingdongBE/domain/feed/service/FacadeFeedServiceTest.java`

기존 테스트 수정:
- [ ] getById() 호출 시 viewCount 증가 확인
- [ ] 응답에 likeCount, commentCount, comments 포함 확인

## Acceptance Criteria
- [ ] 모든 테스트 GREEN
- [ ] 신규 Service 레이어 주요 로직 테스트 커버
- [ ] 예외 케이스 테스트 포함
- [ ] 기존 테스트 깨지지 않음 (FeedQuery 등 시그니처 변경 반영)

## Notes
- 기존 FacadeFeedServiceTest의 FeedQuery 관련 assertion 수정 필요 (필드 추가됨)
- TestContainerSupport 활용하여 실제 DB 연동 테스트
- FixtureMonkey로 테스트 데이터 생성
- S3FileService는 MockitoBean으로 모킹
