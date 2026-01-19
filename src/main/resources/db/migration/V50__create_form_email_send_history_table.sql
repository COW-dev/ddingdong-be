-- 폼 이메일 전송 기록 테이블 생성
CREATE TABLE form_email_send_history
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    form_application_status VARCHAR(255) NOT NULL,
    email_content           TEXT         NULL,
    form_id                 BIGINT       NOT NULL,
    FOREIGN KEY (form_id) REFERENCES form (id) ON DELETE CASCADE
);

-- email_send_history 테이블에 form_email_send_history_id 컬럼 추가
ALTER TABLE email_send_history
    ADD COLUMN form_email_send_history_id BIGINT NULL;

ALTER TABLE email_send_history
    ADD CONSTRAINT fk_email_send_history_form_email_send_history
        FOREIGN KEY (form_email_send_history_id) REFERENCES form_email_send_history (id) ON DELETE CASCADE;
