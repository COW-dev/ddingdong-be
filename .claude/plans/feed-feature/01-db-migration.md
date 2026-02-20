# Task 01: Flyway DB 마이그레이션

## Overview
피드 좋아요, 피드 댓글 테이블 생성 및 Feed 테이블에 view_count 컬럼 추가를 위한 Flyway 마이그레이션 스크립트를 작성한다.

## Dependencies
- 없음 (첫 번째 태스크)

## Blocked By
- 없음

## Files to Create/Modify

### 1. `src/main/resources/db/migration/V54__create_feed_like_table.sql`

```sql
CREATE TABLE feed_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feed_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_feed_like_feed_id FOREIGN KEY (feed_id) REFERENCES feed(id),
    CONSTRAINT fk_feed_like_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_feed_like_feed_user UNIQUE (feed_id, user_id)
);
```

### 2. `src/main/resources/db/migration/V55__create_feed_comment_table.sql`

```sql
CREATE TABLE feed_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feed_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_feed_comment_feed_id FOREIGN KEY (feed_id) REFERENCES feed(id),
    CONSTRAINT fk_feed_comment_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 3. `src/main/resources/db/migration/V56__alter_feed_add_view_count.sql`

```sql
ALTER TABLE feed ADD COLUMN view_count BIGINT NOT NULL DEFAULT 0;
```

## Acceptance Criteria
- [ ] V54, V55, V56 마이그레이션 파일이 정상적으로 생성됨
- [ ] feed_like 테이블에 feed_id + user_id 유니크 제약조건 존재
- [ ] feed_comment 테이블에 content 컬럼 VARCHAR(500)
- [ ] feed 테이블에 view_count 컬럼 DEFAULT 0
- [ ] 로컬 환경에서 Flyway 마이그레이션 정상 실행 확인

## Notes
- 기존 Flyway 버전은 V53까지 존재 (V52: pair_game_applier, V53: alter pair_game_applier)
- feed_like의 unique constraint로 동일 유저의 중복 좋아요 DB 레벨에서 방지
- soft delete 패턴 적용 (deleted_at 컬럼)
- feed_like의 unique constraint와 soft delete 충돌 주의: soft delete된 레코드도 unique에 포함되므로, 좋아요 취소 시 물리 삭제(hard delete) 적용 검토 필요
