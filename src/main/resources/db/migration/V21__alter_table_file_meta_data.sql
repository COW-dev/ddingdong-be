ALTER TABLE file_meta_data
    ADD entity_id BIGINT NULL;

ALTER TABLE file_meta_data
    ADD domain_type VARCHAR(255) NULL;

ALTER TABLE file_meta_data
    ADD file_status VARCHAR(255) NULL;

ALTER TABLE file_meta_data
    ADD id BINARY(16) NULL;

ALTER TABLE file_meta_data
    ADD `key` VARCHAR(255) NULL;

ALTER TABLE file_meta_data
    ADD file_name VARCHAR(255) NULL;

ALTER TABLE file_meta_data
    MODIFY file_status VARCHAR (255) NOT NULL;

ALTER TABLE file_meta_data
    MODIFY `key` VARCHAR (255) NOT NULL;

CREATE INDEX IDX_file_meta_data_domain_type_entity_id_file_status ON file_meta_data (domain_type, entity_id, file_status);

ALTER TABLE file_meta_data
    DROP COLUMN file_id;

ALTER TABLE file_meta_data
    ADD PRIMARY KEY (id);
