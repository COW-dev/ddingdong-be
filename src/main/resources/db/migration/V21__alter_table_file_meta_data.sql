ALTER TABLE file_meta_data
    DROP COLUMN file_id,
    ADD entity_id BIGINT NULL,
    ADD domain_type VARCHAR(255) NULL,
    ADD id BINARY(16) NULL,
    ADD file_key VARCHAR(255) NOT NULL,
    ADD file_name VARCHAR(255) NOT NULL,
    ADD file_status VARCHAR(255) NOT NULL,
    MODIFY file_category VARCHAR(255) NULL;

CREATE INDEX IDX_file_meta_data_domain_type_entity_id_file_status
    ON file_meta_data (domain_type, entity_id, file_status);

ALTER TABLE file_meta_data
    ADD PRIMARY KEY (id);
