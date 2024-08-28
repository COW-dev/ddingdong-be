-- 0. Create table file_meta_data
create table file_meta_data
(
    id          bigint auto_increment
        primary key,
    created_at  timestamp    null,
    updated_at  timestamp    null,
    file_category varchar(255) null,
    file_id     varchar(255) not null,
    file_name   varchar(255) not null
);

ALTER TABLE club
-- 1. Add new columns
    ADD COLUMN introduction_image_url VARCHAR(255) NULL AFTER introduction,
    ADD COLUMN profile_image_url VARCHAR(255) NULL AFTER phone_number,

-- 2. Modify existing columns to set them as NOT NULL
    MODIFY COLUMN activity VARCHAR(255) NOT NULL,
    MODIFY COLUMN category VARCHAR(255) NOT NULL,
    MODIFY COLUMN introduction VARCHAR(255) NOT NULL,
    MODIFY COLUMN leader VARCHAR(255) NOT NULL,
    MODIFY COLUMN name VARCHAR(255) NOT NULL,
    MODIFY COLUMN regular_meeting VARCHAR(255) NOT NULL,
    MODIFY COLUMN tag VARCHAR(255) NOT NULL,

-- 3. Change data type and set default value for score column
    MODIFY COLUMN score DECIMAL(10, 3) NOT NULL DEFAULT 0.000,

-- 4. Modify the deleted_at column to datetime(6)
    MODIFY COLUMN deleted_at DATETIME(6) NULL;
