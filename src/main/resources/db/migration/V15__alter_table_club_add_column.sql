ALTER TABLE club
    ADD introduction_image_key VARCHAR(255) NULL;

ALTER TABLE club
    ADD profile_image_key VARCHAR(255) NULL;

ALTER TABLE club
    ADD CONSTRAINT UK_club_user UNIQUE (user_id);
