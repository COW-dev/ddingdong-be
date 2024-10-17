ALTER TABLE banner
    ADD mobile_image_key VARCHAR(255) NULL;

ALTER TABLE banner
    ADD web_image_key VARCHAR(255) NULL;

ALTER TABLE banner
    DROP COLUMN color_code;

ALTER TABLE banner
    DROP COLUMN sub_title;

ALTER TABLE banner
    DROP COLUMN title;
