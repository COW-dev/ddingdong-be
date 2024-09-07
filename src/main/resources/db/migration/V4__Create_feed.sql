CREATE TABLE feed (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        activity_content VARCHAR(100) NOT NULL,
        thumbnail_url VARCHAR(255) NOT NULL,
        feed_type VARCHAR(10) NOT NULL,
        deleted_at DATETIME,
        club_id BIGINT,
        created_at DATETIME NOT NULL,
        updated_at DATETIME NOT NULL,
        CONSTRAINT fk_feed_club_id FOREIGN KEY (club_id) REFERENCES club(id)
);
