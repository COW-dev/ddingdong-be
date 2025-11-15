-- 이메일 전송 이력 관리를 위한 테이블 생성
CREATE TABLE email_send_history
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    form_application_id  BIGINT                              NOT NULL,
    status               VARCHAR(255)                        NOT NULL,
    retry_count          INT                                 NOT NULL DEFAULT 0,
    message_tracking_id  VARCHAR(255)                        NULL,
    sent_at              DATETIME                            NULL,
    created_at           TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (form_application_id) REFERENCES form_application (id) ON DELETE CASCADE
);
