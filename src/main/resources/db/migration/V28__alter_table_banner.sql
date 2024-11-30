ALTER TABLE banner
    ADD link VARCHAR(255) NULL;

ALTER TABLE banner
    DROP COLUMN mobile_image_key;

ALTER TABLE banner
    DROP COLUMN web_image_key;

ALTER TABLE banner
    DROP COLUMN file_category;
