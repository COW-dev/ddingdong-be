CREATE TABLE feed_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feed_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_feed_like_feed_id FOREIGN KEY (feed_id) REFERENCES feed(id),
    CONSTRAINT fk_feed_like_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uk_feed_like_feed_user UNIQUE (feed_id, user_id)
);
