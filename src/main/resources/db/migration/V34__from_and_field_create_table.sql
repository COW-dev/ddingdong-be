CREATE TABLE form
(
    id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(255),
    start_date    DATE         NOT NULL,
    end_date      DATE         NOT NULL,
    has_interview BOOLEAN      NOT NULL,
    club_id       BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_form_club FOREIGN KEY (club_id) REFERENCES club (id) ON DELETE CASCADE
);

CREATE TABLE form_field
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question   VARCHAR(255) NOT NULL,
    field_type VARCHAR(50)  NOT NULL,
    required   BOOLEAN      NOT NULL,
    field_order    INT          NOT NULL,
    section    VARCHAR(255) NOT NULL,
    options    TEXT,
    form_id    BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_form_field_form FOREIGN KEY (form_id) REFERENCES form (id) ON DELETE CASCADE
);
