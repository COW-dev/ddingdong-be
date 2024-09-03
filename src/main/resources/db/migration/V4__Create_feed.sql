CREATE TABLE feed (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        activity_content TEXT NOT NULL,
        thumbnail_url VARCHAR(255) NOT NULL,
        feed_type TINYINT NOT NULL,
        deleted_at DATETIME,
        club_id BIGINT,
        created_at DATETIME NOT NULL,
        updated_at DATETIME NOT NULL,
        FOREIGN KEY (club_id) REFERENCES club(id)
);
