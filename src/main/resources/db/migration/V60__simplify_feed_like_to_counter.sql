ALTER TABLE feed ADD COLUMN like_count BIGINT NOT NULL DEFAULT 0;

UPDATE feed f SET f.like_count = (
    SELECT COUNT(*) FROM feed_like fl WHERE fl.feed_id = f.id
);

DROP TABLE feed_like;
