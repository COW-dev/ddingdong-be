# Task 10: SecurityConfig 라우팅 업데이트

## Overview
신규 API 엔드포인트에 대한 SecurityConfig 라우팅을 점검하고 필요시 업데이트한다.

## Dependencies
- Task 06, 07, 08 (API 구현 완료 후)

## Files to Modify

### `common/config/SecurityConfig.java`

**현재 상태 분석:**
- `GET /server/feeds/**` -> permitAll (기존, 유지)
- `/server/admin/**` -> ROLE_ADMIN (기존, 유지 -> 랭킹 API 자동 적용)
- `/server/central/**` -> ROLE_CLUB (기존, 유지 -> 동아리 현황 API 자동 적용)
- `POST/DELETE /server/feeds/{feedId}/likes` -> anyRequest().authenticated() (인증 필요, 자동 적용)
- `POST/DELETE /server/feeds/{feedId}/comments` -> anyRequest().authenticated() (인증 필요, 자동 적용)

**결론: SecurityConfig 변경 불필요**

모든 신규 API가 기존 SecurityConfig 규칙에 의해 올바르게 보호됨:
1. 좋아요/댓글 POST/DELETE -> authenticated() (모든 인증된 사용자)
2. 총동연 랭킹 -> ROLE_ADMIN
3. 동아리 현황 -> ROLE_CLUB

**검증 사항:**
- GET `/server/feeds/{feedId}/likes` 같은 읽기 API가 없으므로 permitAll과 충돌 없음
- 좋아요/댓글은 POST/DELETE만 있으므로 기존 GET permitAll 규칙과 무관

## Acceptance Criteria
- [ ] 비인증 사용자가 `POST /server/feeds/{feedId}/likes` 접근 시 401
- [ ] 비인증 사용자가 `POST /server/feeds/{feedId}/comments` 접근 시 401
- [ ] ROLE_CLUB 사용자가 총동연 랭킹 API 접근 시 403
- [ ] ROLE_ADMIN 사용자가 총동연 랭킹 API 정상 접근
- [ ] ROLE_CLUB 사용자가 동아리 현황 API 정상 접근

## Notes
- 이 태스크는 검증 위주이며, 코드 변경이 필요 없을 가능성 높음
- 만약 좋아요/댓글 API에 비인증 사용자(ROLE_USER 등)도 접근 가능해야 한다면 SecurityConfig 수정 필요
