CREATE INDEX idx_feed_comment_feed_id_uuid ON feed_comment (feed_id, uuid);
CREATE INDEX idx_feed_comment_feed_id_created_at ON feed_comment (feed_id, created_at);
