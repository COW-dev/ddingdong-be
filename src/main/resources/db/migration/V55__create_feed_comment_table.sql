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
