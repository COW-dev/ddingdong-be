ALTER TABLE vod_processing_job
    ADD file_meta_data_id BINARY(16) NULL;

ALTER TABLE vod_processing_job
    ADD CONSTRAINT UK_VOD_PROCESSING_JOB_FILE_META_DATA UNIQUE (file_meta_data_id);

ALTER TABLE vod_processing_job
    ADD CONSTRAINT FK_VOD_PROCESSING_JOB_FILE_META_DATA_FILE_META_DATA_ID FOREIGN KEY (file_meta_data_id) REFERENCES file_meta_data (id);
