ALTER TABLE file_meta_data
    DROP COLUMN file_name;

ALTER TABLE file_meta_data
    MODIFY file_category VARCHAR (255) NOT NULL;
