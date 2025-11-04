ALTER TABLE fix_zone_comment
    DROP FOREIGN KEY FK_fix_zone_comment_club_id;

ALTER TABLE fix_zone_comment
    DROP COLUMN club_id;

ALTER TABLE fix_zone_comment
    ADD COLUMN user_id BIGINT NULL DEFAULT 1 AFTER id;

ALTER TABLE fix_zone_comment
    ADD CONSTRAINT FK_fix_zone_comment_user_id
        FOREIGN KEY (user_id) REFERENCES users (id);
