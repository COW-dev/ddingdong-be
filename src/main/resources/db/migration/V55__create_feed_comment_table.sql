CREATE TABLE feed_comment (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    feed_id        BIGINT       NOT NULL,
    uuid           VARCHAR(36)  NOT NULL,
    anonymous_number INT        NOT NULL,
    content        VARCHAR(500) NOT NULL,
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL,
    deleted_at     TIMESTAMP    NULL,
    CONSTRAINT fk_feed_comment_feed_id FOREIGN KEY (feed_id) REFERENCES feed(id)
);
