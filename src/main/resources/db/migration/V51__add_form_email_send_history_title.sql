ALTER TABLE form_email_send_history
    ADD COLUMN title VARCHAR(255) NULL;

UPDATE form_email_send_history
SET title = '제목 없음'
WHERE title IS NULL;

ALTER TABLE form_email_send_history
    MODIFY title VARCHAR(255) NOT NULL;