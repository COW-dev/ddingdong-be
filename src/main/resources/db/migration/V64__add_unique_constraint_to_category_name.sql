ALTER TABLE category
    ADD CONSTRAINT uk_category_name UNIQUE (name);
