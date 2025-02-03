CREATE TABLE form_application
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(50)  NOT NULL,
    student_number VARCHAR(50)  NOT NULL,
    status         VARCHAR(50)  NOT NULL,
    form_id        BIGINT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_application_form FOREIGN KEY (form_id) REFERENCES form (id) ON DELETE CASCADE
);

CREATE TABLE form_answer
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    value          TEXT NULL,
    application_id    BIGINT,
    field_id       BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_answer_application FOREIGN KEY (application_id) REFERENCES form_application (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_field FOREIGN KEY (field_id) REFERENCES form_field (id) ON DELETE CASCADE
);
