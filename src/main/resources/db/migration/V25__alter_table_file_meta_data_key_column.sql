ALTER TABLE file_meta_data
    ADD file_key VARCHAR(255) NULL;

ALTER TABLE file_meta_data
    MODIFY file_key VARCHAR (255) NOT NULL;

ALTER TABLE file_meta_data
DROP COLUMN `key`;

ALTER TABLE file_meta_data
    MODIFY file_category VARCHAR (255) NULL;

ALTER TABLE file_meta_data
    MODIFY file_name VARCHAR (255) NOT NULL;
