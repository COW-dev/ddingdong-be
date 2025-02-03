CREATE TABLE form_response
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    submitted_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    name           VARCHAR(50)  NOT NULL,
    student_number VARCHAR(50)  NOT NULL,
    status         VARCHAR(50)  NOT NULL,
    form_id        BIGINT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_response_form FOREIGN KEY (form_id) REFERENCES form (id) ON DELETE CASCADE
);

CREATE TABLE form_answer
(
    id             BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    value          VARCHAR(1500) NULL,
    value_type     VARCHAR(50)  NOT NULL,
    response_id    BIGINT,
    field_id       BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL,
    CONSTRAINT fk_answer_response FOREIGN KEY (response_id) REFERENCES form_response (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_field FOREIGN KEY (field_id) REFERENCES form_field (id) ON DELETE CASCADE
);
