CREATE TABLE category
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    color      VARCHAR(255) NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE TABLE event
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    repeat_type VARCHAR(255) NOT NULL,
    category_id BIGINT       NULL,
    deleted_at  TIMESTAMP    NULL,
    CONSTRAINT fk_event_category_id FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE INDEX idx_event_period ON event (start_date, end_date);
CREATE INDEX idx_event_category_id ON event (category_id);
