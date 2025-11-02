ALTER TABLE fix_zone_comment
    DROP FOREIGN KEY FKc2d91n1o7eindrcl3io79hlj7;

ALTER TABLE fix_zone_comment
    DROP COLUMN club_id;

ALTER TABLE fix_zone_comment
    ADD COLUMN user_id BIGINT NULL AFTER id;

ALTER TABLE fix_zone_comment
    ADD CONSTRAINT fk_fix_zone_comment_user_id
        FOREIGN KEY (user_id) REFERENCES users (id);
