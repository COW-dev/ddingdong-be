create table file_meta_data
(
    file_id       binary(16)  not null
        primary key,
    created_at    timestamp    null,
    updated_at    timestamp    null,
    file_category varchar(255) not null,
    file_name     varchar(255) not null
);
