-- 배너 타입 컬럼 추가 (기존 배너는 MANUAL)
ALTER TABLE banner ADD COLUMN banner_type VARCHAR(20) NOT NULL DEFAULT 'MANUAL';

-- 자동생성 배너는 user 없이 생성되므로 nullable 보장
ALTER TABLE banner MODIFY user_id BIGINT NULL;
