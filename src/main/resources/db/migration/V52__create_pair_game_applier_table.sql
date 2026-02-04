CREATE TABLE pair_game_applier
(
    id                       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                     VARCHAR(50)  NOT NULL,
    department               VARCHAR(50)  NOT NULL,
    student_number           VARCHAR(50)  NOT NULL UNIQUE,
    phone_number             VARCHAR(50)  NOT NULL,
    student_fee_image_url    VARCHAR(255) NOT NULL,
    created_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at               TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    deleted_at               TIMESTAMP NULL DEFAULT NULL
);
