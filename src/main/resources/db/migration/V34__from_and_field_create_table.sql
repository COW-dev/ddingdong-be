CREATE TABLE form
(
    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    club_id     BIGINT,
    CONSTRAINT fk_form_club FOREIGN KEY (club_id) REFERENCES club (id) ON DELETE CASCADE
);

CREATE TABLE form_field
(
    id         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    question   VARCHAR(255) NOT NULL,
    field_type VARCHAR(50)  NOT NULL,
    required   BOOLEAN      NOT NULL,
    `order`    INT          NOT NULL,
    section    VARCHAR(255) NOT NULL,
    options    TEXT,
    form_id    BIGINT,
    CONSTRAINT fk_form_field_form FOREIGN KEY (form_id) REFERENCES form (id) ON DELETE CASCADE
);
