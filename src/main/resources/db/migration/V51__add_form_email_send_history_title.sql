ALTER TABLE form_email_send_history
    ADD COLUMN title VARCHAR(255) NOT NULL AFTER form_application_status;