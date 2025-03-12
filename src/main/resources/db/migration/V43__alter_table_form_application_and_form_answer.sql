ALTER TABLE form_answer
    ADD deleted_at timestamp NULL;

ALTER TABLE form_application
    ADD deleted_at timestamp NULL;

ALTER TABLE form_application
    MODIFY email VARCHAR (255) NOT NULL;

ALTER TABLE form_application
    MODIFY phone_number VARCHAR (255) NOT NULL;
