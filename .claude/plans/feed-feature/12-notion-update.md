# Task 12: Notion ERD/API 명세 업데이트

## Overview
신규 테이블 ERD, 변경된 API 명세, 신규 API 명세를 Notion 띵동 프로젝트 문서에 기록한다.

## Dependencies
- Task 01~11 모두 완료 후

## Notion Access Info
- **Notion API Key**: `~/.mcp.json` 파일의 `NOTION_API_KEY` 참조
- **띵동 페이지 ID**: `30d0de21-3cb0-8005-ab3a-cdfe6c6cf678`
- **접근 방식**: MCP 도구 401 오류 발생 가능 -> curl 직접 호출로 대체

## 기록 내용

### 1. ERD 업데이트 - 신규 테이블

**feed_like 테이블:**
| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | |
| feed_id | BIGINT | FK(feed.id), NOT NULL | |
| user_id | BIGINT | FK(users.id), NOT NULL | |
| created_at | TIMESTAMP | NOT NULL | |
| updated_at | TIMESTAMP | NOT NULL | |
| deleted_at | TIMESTAMP | NULLABLE | |
| | | UNIQUE(feed_id, user_id) | |

**feed_comment 테이블:**
| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | |
| feed_id | BIGINT | FK(feed.id), NOT NULL | |
| user_id | BIGINT | FK(users.id), NOT NULL | |
| content | VARCHAR(500) | NOT NULL | |
| created_at | TIMESTAMP | NOT NULL | |
| updated_at | TIMESTAMP | NOT NULL | |
| deleted_at | TIMESTAMP | NULLABLE | Soft delete |

**feed 테이블 변경:**
| Column | Type | Constraint | Description |
|--------|------|-----------|-------------|
| view_count | BIGINT | NOT NULL, DEFAULT 0 | 추가됨 |

### 2. 신규 API 명세

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/server/feeds/{feedId}/likes` | USER | 피드 좋아요 |
| DELETE | `/server/feeds/{feedId}/likes` | USER | 피드 좋아요 취소 |
| POST | `/server/feeds/{feedId}/comments` | USER | 피드 댓글 작성 |
| DELETE | `/server/feeds/{feedId}/comments/{commentId}` | USER | 피드 댓글 삭제 |
| GET | `/server/admin/feed-rankings/monthly?year=&month=` | ADMIN | 월별 피드 랭킹 |
| GET | `/server/admin/feed-rankings/winners?year=` | ADMIN | 지난 1위 목록 |
| GET | `/server/central/my/feed-rankings?year=` | CLUB | 내 피드 개별 랭킹 |
| GET | `/server/central/my/feed-rankings/monthly-best` | CLUB | 이달의 피드 현황 |

### 3. 변경된 API 명세

**GET /server/feeds (전체 피드 목록)**
- 응답 필드 추가: `likeCount`, `commentCount`

**GET /server/feeds/{feedId} (피드 상세)**
- 응답 필드 추가: `likeCount`, `commentCount`, `comments[]`
- comments 구조: `{id, content, authorName, createdAt}`

**GET /server/clubs/{clubId}/feeds (동아리별 피드)**
- 응답 필드 추가: `likeCount`, `commentCount`

## Implementation Notes

Notion API 호출 예시 (curl):
```bash
# 1. 띵동 페이지 하위 페이지 검색
curl -X POST 'https://api.notion.com/v1/search' \
  -H 'Authorization: Bearer ${NOTION_API_KEY}' \
  -H 'Notion-Version: 2022-06-28' \
  -H 'Content-Type: application/json' \
  -d '{"query": "ERD", "filter": {"property": "object", "value": "page"}}'

# 2. 페이지에 블록 추가
curl -X PATCH 'https://api.notion.com/v1/blocks/{block_id}/children' \
  -H 'Authorization: Bearer ${NOTION_API_KEY}' \
  -H 'Notion-Version: 2022-06-28' \
  -H 'Content-Type: application/json' \
  -d '{...}'
```

## Acceptance Criteria
- [ ] 신규 테이블(feed_like, feed_comment) ERD가 Notion에 기록됨
- [ ] feed 테이블 view_count 컬럼 추가 사항 반영
- [ ] 8개 신규 API 명세가 Notion에 기록됨
- [ ] 3개 변경된 API의 수정 사항이 Notion에 기록됨
- [ ] Notion 페이지 접근 및 편집 정상 동작

## Notes
- Notion MCP 도구가 401 오류 발생할 수 있으므로 curl 직접 호출 우선
- `~/.mcp.json`에서 NOTION_API_KEY 읽어서 사용
- 기존 ERD/API 명세 페이지 구조를 파악한 후 적절한 위치에 추가
