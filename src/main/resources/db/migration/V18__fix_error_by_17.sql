ALTER TABLE club
    DROP COLUMN activity_report_image_key;

ALTER TABLE activity_report
    ADD activity_report_image_key VARCHAR(255) NULL;
