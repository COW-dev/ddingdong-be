CREATE TABLE vod_processing_job
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         timestamp             NULL,
    updated_at         timestamp             NULL,
    notification_id    BIGINT                NULL,
    convert_job_id     VARCHAR(255)          NOT NULL,
    user_id            VARCHAR(255)          NOT NULL,
    convert_job_status VARCHAR(255)          NULL,
    CONSTRAINT PK_vod_processing_job PRIMARY KEY (id)
);

CREATE TABLE vod_processing_notification
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          timestamp             NULL,
    updated_at          timestamp             NULL,
    expired_at          datetime              NULL,
    sent_at             datetime              NULL,
    retry_count         INT                   NOT NULL,
    notification_status VARCHAR(255)          NOT NULL,
    CONSTRAINT PK_vod_processing_notification PRIMARY KEY (id)
);

ALTER TABLE vod_processing_job
    ADD CONSTRAINT UC_vod_processing_job_notification UNIQUE (notification_id);

ALTER TABLE vod_processing_job
    ADD CONSTRAINT FK_vod_processing_job_vod_processing_notification_id FOREIGN KEY (notification_id) REFERENCES vod_processing_notification (id);
