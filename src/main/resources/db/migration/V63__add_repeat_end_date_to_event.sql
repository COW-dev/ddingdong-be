ALTER TABLE event
    ADD COLUMN repeat_end_date DATE NULL AFTER end_date;

UPDATE event
SET repeat_end_date = end_date;

ALTER TABLE event
    MODIFY COLUMN repeat_end_date DATE NOT NULL;

CREATE INDEX idx_event_repeat_period ON event (start_date, repeat_end_date);
