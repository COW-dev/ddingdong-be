ALTER TABLE form
    ADD deleted_at timestamp NULL;

ALTER TABLE form_field
    ADD deleted_at timestamp NULL;
