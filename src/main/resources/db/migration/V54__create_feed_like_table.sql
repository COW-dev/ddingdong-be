CREATE TABLE feed_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feed_id BIGINT NOT NULL,
    uuid VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_feed_like_feed_id FOREIGN KEY (feed_id) REFERENCES feed(id),
    CONSTRAINT uidx_feed_like_feed_uuid UNIQUE (feed_id, uuid)
);
